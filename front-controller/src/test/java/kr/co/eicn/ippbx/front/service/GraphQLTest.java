package kr.co.eicn.ippbx.front.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.co.eicn.ippbx.front.service.api.application.kms.KmsGraphQLInterface;
import org.junit.jupiter.api.Test;

public class GraphQLTest {

    final private KmsGraphQLInterface kmsGraphQLInterface = new KmsGraphQLInterface();
    @Test
    public void call() throws JsonProcessingException {
        /*KmsGraphQLInterface.GetKnowledgeCategoriesClass getKnowledgeCategoriesClass = kmsGraphQLInterface.getCategory();
        System.out.println(getKnowledgeCategoriesClass);*/

        KmsGraphQLInterface.GetKnowledgeList getKnowledgeClass = kmsGraphQLInterface.getSearchKnowledge("", null);
        System.out.println(getKnowledgeClass);

        /*KmsGraphQLInterface.GetKnowledgeDetail getKnowledgeClass = kmsGraphQLInterface.getSearchIdKnowledge(679);
        System.out.println(getKnowledgeClass);*/

        /*KmsGraphQLInterface.GetRecentChangedKnowledgeList getRecentChangedKnowledgeList = kmsGraphQLInterface.getRecentChangedKnowledge();
        System.out.println(getRecentChangedKnowledgeList);*/

        /*KmsGraphQLInterface.GetMyBookmarkedKnowledgeList getRecentChangedKnowledgeList = kmsGraphQLInterface.getMyBookmarkedKnowledge();
        System.out.println(getRecentChangedKnowledgeList);*/

        /*KmsGraphQLInterface.GetTopHitKnowledgeList getTopHitKnowledgeList = kmsGraphQLInterface.getTopHitKnowledge("2023-06-01", "2023-06-13");
        System.out.println(getTopHitKnowledgeList);*/

        /*KmsGraphQLInterface.GetTopHitKnowledgeTagsList getTopHitKnowledgeTagsList = kmsGraphQLInterface.getTopHitKnowledgeTags("2023-06-01", "2023-06-13");
        System.out.println(getTopHitKnowledgeTagsList);*/
    }

}
