package jira.tool.db.mapper;


import jira.tool.db.model.Story;
import jira.tool.db.model.User;

import java.util.List;

public interface JiraMapper {
    // Daily report
    List<Story> getDevelopedStoryList();
    List<Story> getNoDueDateStoryList();

    // Half-weekly report
    List<Story> getQAStoryList();
    List<Story> getUnDevelopedStoryList();

    // Weekly report
    List<Story> getReleasePlanStoryList();
    List<Story> getStartPlanStoryList();
    List<Story> getReleasedStoryList();
    List<Story> getStartDateList();
    List<Story> getReleaseDateList();
    List<Story> getCustomerList();
    List<Story> getCustomerOptionList();
    List<Story> getCustomerOptionListOnlyEnabled();

    // For ea2jira and jira2ea
    List<Story> getEpicList();
    List<Story> getStoryList();
    List<Story> getPMOStoryList();
    List<Story> getEAGUIDList();
    List<Story> getPMOLabelList();

    List<Story> getIssueList();

    List<User> getUserList();
    List<User> getTeamList();
    List<User> getTeamMembersCountList();
}
