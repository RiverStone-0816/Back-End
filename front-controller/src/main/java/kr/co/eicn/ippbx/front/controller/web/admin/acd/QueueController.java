package kr.co.eicn.ippbx.front.controller.web.admin.acd;

import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.acd.QueueApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.ScheduleGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.tel.NumberApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.enums.BlendingMode;
import kr.co.eicn.ippbx.model.enums.CallDistributionStrategy;
import kr.co.eicn.ippbx.model.form.QueueFormRequest;
import kr.co.eicn.ippbx.model.form.QueueUpdateBlendingFormRequest;
import kr.co.eicn.ippbx.model.search.QueueSearchRequest;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/acd/queue")
public class QueueController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(QueueController.class);

    private final QueueApiInterface apiInterface;
    private final NumberApiInterface numberApiInterface;
    private final ScheduleGroupApiInterface scheduleGroupApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") QueueSearchRequest search) throws IOException, ResultFailException {
        search.setLimit(1000);
        final Pagination<QueueSummaryResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        return "admin/acd/queue/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") QueueFormRequest form) throws IOException, ResultFailException {
        final List<String> numbers = numberApiInterface.typeNumbers((byte) 0, "").stream().map(SummaryNumber070Response::getNumber).distinct().sorted().collect(Collectors.toList());
        model.addAttribute("numbers", numbers);
        final Map<String, String> services = apiInterface.services().stream().collect(Collectors.toMap(ServiceList::getSvcNumber, ServiceList::getSvcName));

        model.addAttribute("services", new MapToLinkedHashMap().toLinkedHashMapByValue(services));
        final Map<String, String> subGroups = apiInterface.subGroups().stream().collect(Collectors.toMap(SummaryQueueResponse::getName, SummaryQueueResponse::getHanName));
        model.addAttribute("subGroups", new MapToLinkedHashMap().toLinkedHashMapByValue(subGroups));

        final List<SummaryPersonResponse> addOnPersons = apiInterface.addOnPersons(null);
        model.addAttribute("addOnPersons", addOnPersons);

        Map<String, String> strategyOptions;
        if(g.getServiceKind().equals("CC") && g.getUsingServices().contains("TYPE2")) {
            strategyOptions = FormUtils.optionsOfCode(CallDistributionStrategy.FEWESTCALLS,CallDistributionStrategy.LEASTRECENT,CallDistributionStrategy.RANDOM
            ,CallDistributionStrategy.RINGALL,CallDistributionStrategy.RRMEMORY);
        } else {
            strategyOptions = FormUtils.optionsOfCode(CallDistributionStrategy.class);
        }

        model.addAttribute("strategyOptions", new MapToLinkedHashMap().toLinkedHashMapByValue(strategyOptions));
        final Map<String, String> ringBackTones = apiInterface.ringBackTone().stream().collect(Collectors.toMap(SummaryMohListResponse::getCategory, SummaryMohListResponse::getMohName));
        model.addAttribute("ringBackTones", new MapToLinkedHashMap().toLinkedHashMapByValue(ringBackTones));
        final Map<Integer, String> sounds = scheduleGroupApiInterface.addSoundList().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", new MapToLinkedHashMap().toLinkedHashMapByValue(sounds));
        final Map<String, String> contexts = apiInterface.context().stream().collect(Collectors.toMap(SummaryContextInfoResponse::getContext, SummaryContextInfoResponse::getName));
        model.addAttribute("contexts", new MapToLinkedHashMap().toLinkedHashMapByValue(contexts));
        final Map<String, String> queues = apiInterface.addQueueNames().stream().collect(Collectors.toMap(SummaryQueueResponse::getName, SummaryQueueResponse::getHanName));
        model.addAttribute("queues", new MapToLinkedHashMap().toLinkedHashMapByValue(queues));

        return "admin/acd/queue/modal";
    }

    @GetMapping("{name}/modal")
    public String modal(Model model, @PathVariable String name, @ModelAttribute("form") QueueFormRequest form) throws IOException, ResultFailException {
        final QueueDetailResponse entity = apiInterface.get(name);
        if (entity.getRetryMaxCount() > 0) form.setIsRetry(true);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);

        final List<String> numbers = numberApiInterface.typeNumbers((byte) 0, entity.getHost()).stream().map(SummaryNumber070Response::getNumber).distinct().collect(Collectors.toList());
        model.addAttribute("numbers", numbers);
        final Map<String, String> services = apiInterface.services(entity.getHost()).stream().collect(Collectors.toMap(ServiceList::getSvcNumber, ServiceList::getSvcName));
        model.addAttribute("services", new MapToLinkedHashMap().toLinkedHashMapByValue(services));
        final Map<String, String> subGroups = apiInterface.subGroups(entity.getName()).stream().collect(Collectors.toMap(SummaryQueueResponse::getName, SummaryQueueResponse::getHanName));
        model.addAttribute("subGroups", new MapToLinkedHashMap().toLinkedHashMapByValue(subGroups));
        final List<SummaryPersonResponse> addOnPersons = apiInterface.addOnPersons(entity.getName());
        model.addAttribute("addOnPersons", addOnPersons);

        Map<String, String> strategyOptions;
        if(g.getServiceKind().equals("CC") && g.getUsingServices().contains("TYPE2")) {
            strategyOptions = FormUtils.optionsOfCode(CallDistributionStrategy.FEWESTCALLS,CallDistributionStrategy.LEASTRECENT,CallDistributionStrategy.RANDOM
                    ,CallDistributionStrategy.RINGALL,CallDistributionStrategy.RRMEMORY,CallDistributionStrategy.SKILL);
        } else {
            strategyOptions = FormUtils.optionsOfCode(CallDistributionStrategy.class);
        }
        model.addAttribute("strategyOptions", new MapToLinkedHashMap().toLinkedHashMapByValue(strategyOptions));

        final Map<String, String> ringBackTones = apiInterface.ringBackTone().stream().collect(Collectors.toMap(SummaryMohListResponse::getCategory, SummaryMohListResponse::getMohName));
        model.addAttribute("ringBackTones", new MapToLinkedHashMap().toLinkedHashMapByValue(ringBackTones));
        final Map<Integer, String> sounds = scheduleGroupApiInterface.addSoundList().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("sounds", new MapToLinkedHashMap().toLinkedHashMapByValue(sounds));
        final Map<String, String> contexts = apiInterface.context().stream().collect(Collectors.toMap(SummaryContextInfoResponse::getContext, SummaryContextInfoResponse::getName));
        model.addAttribute("contexts", new MapToLinkedHashMap().toLinkedHashMapByValue(contexts));
        final Map<String, String> queues = apiInterface.addQueueNames().stream().collect(Collectors.toMap(SummaryQueueResponse::getName, SummaryQueueResponse::getHanName));
        model.addAttribute("queues", new MapToLinkedHashMap().toLinkedHashMapByValue(queues));

        if (StringUtils.isNotEmpty(entity.getNumber()) && !numbers.contains(entity.getNumber()))
            numbers.add(entity.getNumber());
        if (StringUtils.isNotEmpty(entity.getMusiconhold()) && !ringBackTones.containsKey(entity.getMusiconhold()))
            ringBackTones.put(entity.getMusiconhold(), entity.getMusiconholdName());

        return "admin/acd/queue/modal";
    }

    @GetMapping("{name}/modal-blending")
    public String modal(Model model, @PathVariable String name, @ModelAttribute("form") QueueUpdateBlendingFormRequest form) throws IOException, ResultFailException {
        final QueueDetailResponse entity = apiInterface.get(name);
        ReflectionUtils.copy(form, entity);
        model.addAttribute("entity", entity);
        form.setWaitingCount(entity.getBlendingWaitingCount());
        form.setWaitingKeepTime(entity.getBlendingWaitingKeepTime());
        form.setStartHour(entity.getBlendingTimeFromtime() / 60);
        form.setStartMinute(entity.getBlendingTimeFromtime() % 60);
        form.setEndHour(entity.getBlendingTimeTotime() / 60);
        form.setEndMinute(entity.getBlendingTimeTotime() % 60);

        final Map<String, String> blendingModes = FormUtils.optionsOfCode(BlendingMode.class);
        model.addAttribute("blendingModes", new MapToLinkedHashMap().toLinkedHashMapByValue(blendingModes));
        final List<SummaryPersonResponse> addOnPersons = apiInterface.addOnPersons(entity.getName());
        model.addAttribute("addOnPersons", addOnPersons);

        return "admin/acd/queue/modal-blending";
    }
}
