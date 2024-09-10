package kr.co.eicn.ippbx.server.repository.pds;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsGroup;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.CommonPDSCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.pds.tables.records.PdsCustomInfoRecord;
import kr.co.eicn.ippbx.model.entity.pds.PDSCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PDSCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.PDSDataSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PDSGroupRepository;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PdsGroup.PDS_GROUP;
import static kr.co.eicn.ippbx.meta.jooq.pds.tables.PdsCustomInfo.PDS_CUSTOM_INFO;

@Getter
public class PDSCustomInfoRepository extends PDSDbBaseRepository<CommonPDSCustomInfo, PDSCustomInfoEntity, String> {
	protected final Logger logger = LoggerFactory.getLogger(PDSCustomInfoRepository.class);

	private final CommonPDSCustomInfo TABLE;
	@Autowired
	private PDSGroupRepository pdsGroupRepository;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private PBXServerInterface pbxServerInterface;

	public PDSCustomInfoRepository(String table) {
		super(new CommonPDSCustomInfo(table), new CommonPDSCustomInfo(table).PDS_SYS_CUSTOM_ID, PDSCustomInfoEntity.class);
		TABLE = new CommonPDSCustomInfo(table);
	}

	public int createTableIfNotExists() {
		return createTableIfNotExists(dsl);
	}

	public int createTableIfNotExists(DSLContext dslContext) {
		return dslContext.createTableIfNotExists(TABLE.getName())
				.columns(PDS_CUSTOM_INFO.fields())
				.constraint(DSL.constraint(TABLE.PDS_SYS_CUSTOM_ID.getName()).primaryKey(TABLE.PDS_SYS_CUSTOM_ID.getName()))
				.indexes(PDS_CUSTOM_INFO.getIndexes().stream().filter(index -> !"PRIMARY".equals(index.getName())).collect(Collectors.toList()))
				.storage("ENGINE=MyISAM")
				.execute();
	}

	public Pagination<PDSCustomInfoEntity> pagination(PDSDataSearchRequest search) {
		return super.pagination(search, conditions(search));
	}

	public PDSCustomInfoEntity findOne(Integer groupSeq, String id) {
		return findOne(id);
	}

	private List<Condition> conditions(PDSDataSearchRequest search) {
		final List<Condition> conditions = new ArrayList<>();

		if (Objects.nonNull(search.getGroupSeq()))
			conditions.add(TABLE.PDS_SYS_GROUP_ID.eq(search.getGroupSeq()));
		if (search.getCreatedStartDate() != null)
			conditions.add(TABLE.PDS_SYS_UPLOAD_DATE.ge(DSL.timestamp(search.getCreatedStartDate() + " 00:00:00")));
		if (search.getCreatedEndDate() != null)
			conditions.add(TABLE.PDS_SYS_UPLOAD_DATE.le(DSL.timestamp(search.getCreatedEndDate() + " 23:59:59")));

		search.getDbTypeFields().forEach((k, v) -> {
			final Field<?> field = TABLE.field(k);

			if (field == null) {
				logger.warn("invalid type: " + k);
			} else if (field.getType().equals(Date.class) || field.getType().equals(Timestamp.class)) {
				if (v.getStartDate() != null)
					conditions.add(TABLE.field(k, Date.class).greaterOrEqual(v.getStartDate()));
				if (v.getEndDate() != null)
					conditions.add(TABLE.field(k, Date.class).lessOrEqual(v.getEndDate()));
			} else if (k.contains("_INT_") || k.contains("_CONCODE_") || k.contains("_CSCODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
				if (StringUtils.isNotEmpty(v.getKeyword()))
					conditions.add(TABLE.field(k, String.class).eq(v.getKeyword()));
			} else if (k.contains("_STRING_") || k.contains("_NUMBER_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
				if (StringUtils.isNotEmpty(v.getKeyword()))
					conditions.add(TABLE.field(k, String.class).like("%" + v.getKeyword() + "%"));
			} else if (k.contains("_CODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
				if (StringUtils.isNotEmpty(v.getCode()))
					conditions.add(TABLE.field(k, String.class).eq(v.getCode()));
			} else if (k.contains("_MULTICODE_")) { // FIXME: column 타입이 변경되면 에러를 발생시킬수 있다.
				if (StringUtils.isNotEmpty(v.getCode()))
					conditions.add(
							TABLE.field(k, String.class).likeRegex("^" + v.getCode() + ",")
									.or(TABLE.field(k, String.class).likeRegex("^" + v.getCode() + "$"))
									.or(TABLE.field(k, String.class).likeRegex("," + v.getCode() + "$"))
									.or(TABLE.field(k, String.class).likeRegex("," + v.getCode() + ","))
					);
			} else {
				if (StringUtils.isNotEmpty(v.getKeyword()))
					conditions.add(TABLE.field(k, String.class).eq(v.getKeyword()));
			}
		});

		return conditions;
	}

	public static String getTimeStr() {
		Calendar cal = Calendar.getInstance();
		return new java.text.DecimalFormat("00").format(cal.get(Calendar.YEAR)) +
				new java.text.DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1) +
				new java.text.DecimalFormat("00").format(cal.get(Calendar.DATE)) +
				new java.text.DecimalFormat("00").format(cal.get(Calendar.HOUR_OF_DAY)) +
				new java.text.DecimalFormat("00").format(cal.get(Calendar.MINUTE)) +
				new java.text.DecimalFormat("00").format(cal.get(Calendar.SECOND));
	}

	public void insert(PDSCustomInfoFormRequest form) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		final PdsGroup pdsGroup = pdsGroupRepository.findOne(form.getGroupSeq());

		final String id = "PDS_" + form.getGroupSeq() + getTimeStr();

		final InsertSetMoreStep<PdsCustomInfoRecord> query = dsl.insertInto(TABLE)
				.set(TABLE.PDS_SYS_CUSTOM_ID, id)
				.set(TABLE.PDS_SYS_GROUP_ID, form.getGroupSeq())
				.set(TABLE.PDS_SYS_UPLOAD_DATE, DSL.now())
				.set(TABLE.PDS_SYS_UPDATE_DATE, DSL.now())
				.set(TABLE.PDS_SYS_LAST_RESULT_DATE, DSL.now())
				.set(TABLE.PDS_SYS_LAST_CALL_DATE, DSL.now())
				.set(TABLE.PDS_SYS_GROUP_TYPE, pdsGroup.getPdsType())
				.set(TABLE.PDS_SYS_RESULT_TYPE, pdsGroup.getResultType())
				.set(TABLE.PDS_SYS_COMPANY_ID, getCompanyId());

		final List<? extends Class<? extends Serializable>> insertableFieldTypes = Arrays.asList(Date.class, Timestamp.class, Integer.class, String.class);
		for (java.lang.reflect.Field field : form.getClass().getDeclaredFields()) {
			if (!insertableFieldTypes.contains(field.getType()))
				continue;

			final String fieldName = field.getName();
			final Field<?> tableField = TABLE.field("PDS_" + fieldName.toUpperCase());
			if (tableField == null)
				continue;

			final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			final Object invoked = form.getClass().getMethod("get" + capName).invoke(form);

			if (tableField.getType().equals(Date.class)) {
				query.set((Field<Date>) tableField, (Date) invoked);
			} else if (tableField.getType().equals(Timestamp.class)) {
				query.set((Field<Timestamp>) tableField, (Timestamp) invoked);
			} else if (tableField.getType().equals(Integer.class)) {
				query.set((Field<Integer>) tableField, (Integer) invoked);
			} else { // String.class
				query.set((Field<String>) tableField, (String) invoked);
			}
		}

		query.execute();

		dsl.update(PDS_GROUP)
				.set(PDS_GROUP.TOTAL_CNT, PDS_GROUP.TOTAL_CNT.add(1))
				.where(PDS_GROUP.SEQ.eq(pdsGroup.getSeq()))
				.execute();
	}

	public void update(PDSCustomInfoFormRequest form, String id) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		final UpdateSetMoreStep<PdsCustomInfoRecord> query = dsl.update(TABLE)
				.set(TABLE.PDS_SYS_UPDATE_DATE, DSL.now())
				.set(TABLE.PDS_SYS_DAMDANG_ID, g.getUser().getId());

		final List<? extends Class<? extends Serializable>> insertableFieldTypes = Arrays.asList(Date.class, Timestamp.class, Integer.class, String.class);
		for (java.lang.reflect.Field field : form.getClass().getDeclaredFields()) {
			if (!insertableFieldTypes.contains(field.getType()))
				continue;

			final String fieldName = field.getName();
			final Field<?> tableField = TABLE.field("PDS_" + fieldName.toUpperCase());
			if (tableField == null)
				continue;

			final String capName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			final Object invoked = form.getClass().getMethod("get" + capName).invoke(form);

			if (tableField.getType().equals(Date.class)) {
				query.set((Field<Date>) tableField, (Date) invoked);
			} else if (tableField.getType().equals(Timestamp.class)) {
				query.set((Field<Timestamp>) tableField, (Timestamp) invoked);
			} else if (tableField.getType().equals(Integer.class)) {
				query.set((Field<Integer>) tableField, (Integer) invoked);
			} else { // String.class
				query.set((Field<String>) tableField, (String) invoked);
			}
		}

		query.where(TABLE.PDS_SYS_CUSTOM_ID.eq(id)).execute();
	}

	public void deleteData(String id) {
		final PDSCustomInfoEntity entity = findOneIfNullThrow(id);

		delete(entity.getPdsSysCustomId());

		dsl.update(PDS_GROUP)
				.set(PDS_GROUP.TOTAL_CNT, PDS_GROUP.TOTAL_CNT.minus(1))
				.where(PDS_GROUP.SEQ.eq(entity.getPdsSysGroupId()))
				.execute();
	}
}
