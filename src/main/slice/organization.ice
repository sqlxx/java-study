[["java:package:com.maycur.ice"]]
module organization {
    ["java:type:java.util.ArrayList<String>"]sequence<string> stringList;
    ["java:type:java.util.ArrayList<Long>"]sequence<long> longList;
    dictionary<string,int> stringIntMap;

    exception MIceSubsidiaryServiceException {
        string message;
        int errorCode;
    };

    ["java:getset"]
    struct MIceSubsidiary {
        string code;
        bool enabled;
        string name;
        string nameEn;
        string businessCode;
        string principle;
        string baseCcy;
        string parentCode;
        string heirCode;
    };

    ["java:getset"]
    struct MIceSimplifiedSubsidiary {
        string code;
        string name;
        string baseCcy;
    };


    ["java:type:java.util.ArrayList<MIceSubsidiary>"]sequence<MIceSubsidiary> SubsidiaryList;

    exception MIceDepartmentServiceException {
        string message;
        int errorCode;
    };

    ["java:getset"]
    struct MIceDepartment {
        string code;
        string parentCode;
        string heirCode;
        string name;
        string fullName;
        string businessCode;
        string principle;
        int level;
        bool enabled;
        string costCenterCode;
        string nameEn;
        string fullNameEn;
        string source;
        string sourceId;
        string departmentSetCode;
        stringList subsidiaryCodes;
        string subsidiaryName;
        int childrenCount;
        stringList childrenCodes;
    };

    ["java:type:java.util.ArrayList<MIceDepartment>"]sequence<MIceDepartment> DepartmentList;

    struct MIceSubsidiaryDepartment {
        string subsidiaryCode;
        stringList departmentCodes;
    };

    class MIceSubsidiaryTreeNode;

    ["java:type:java.util.ArrayList<MIceSubsidiaryTreeNode>"]sequence<MIceSubsidiaryTreeNode> SubsidiaryTreeNodes;

    class MIceSubsidiaryTreeNode {
        MIceSubsidiary subsidiary;
        SubsidiaryTreeNodes children;
    };

    ["java:getset"]
    struct MIceUserDutyScope {
        string departmentCode;
        string subsidiaryCode;
    };

    ["java:type:java.util.ArrayList<MIceUserDutyScope>"]
    sequence<MIceUserDutyScope> UserDutyScopes;

    struct MIceSubsidiarySearchParam {
        string keyword;
        string startDate;
        string endDate;
        UserDutyScopes dutyScopes;
    };

    struct MIceSubsidiarySearchResult {
        SubsidiaryList subsidiaries;
        bool hasMore;
    };

    struct MIceSubsidiaryAcceptSetting {
        stringList bankCodes;
        stringList ccyCodes;
    };

    interface SubsidiaryServant {
        string saveSubsidiary(string entCode, string userCode, MIceSubsidiary subsidiary) throws MIceSubsidiaryServiceException;
        string createRoot(string entCode, string userCode, string name) throws MIceSubsidiaryServiceException;
        bool deleteSubsidiary(string entCode, string userCode, string code) throws MIceSubsidiaryServiceException;
        idempotent MIceSubsidiary get(string entCode, string code) throws MIceSubsidiaryServiceException;
        idempotent MIceSubsidiary getOrError(string entCode, string code) throws MIceSubsidiaryServiceException;
        idempotent MIceSubsidiaryTreeNode getAllAsTree(string entCode, string status) throws MIceSubsidiaryServiceException;
        idempotent SubsidiaryList getAll(string entCode) throws MIceSubsidiaryServiceException;
        idempotent SubsidiaryList getAllActive(string entCode) throws MIceSubsidiaryServiceException;
        idempotent SubsidiaryList getByCodes(string entCode, stringList codes) throws MIceSubsidiaryServiceException;
        idempotent SubsidiaryList getByName(string entCode, string name) throws MIceSubsidiaryServiceException;
        idempotent SubsidiaryList getByBusinessCode(string entCode, stringList businessCode) throws MIceSubsidiaryServiceException;
        idempotent MIceSubsidiary getRoot(string entCode) throws MIceSubsidiaryServiceException;
        idempotent MIceSubsidiarySearchResult search(string entCode, MIceSubsidiarySearchParam searchParam, int limit) throws MIceSubsidiaryServiceException;
        idempotent SubsidiaryList getByDepartment(string entCode, string departmentCode) throws MIceSubsidiaryServiceException;
        idempotent SubsidiaryList getByPrinciple(string entCode, string principle) throws MIceSubsidiaryServiceException;
        idempotent SubsidiaryList getByDepartmentHeirCode(string entCode, string departmentHeirCode) throws MIceSubsidiaryServiceException;
        bool saveAcceptSetting(string entCode, string userCode, string subsidiaryCode, MIceSubsidiaryAcceptSetting setting) throws MIceSubsidiaryServiceException;
        idempotent MIceSubsidiaryAcceptSetting getAcceptSetting(string entCode, string subsidiaryCode) throws MIceSubsidiaryServiceException;
        idempotent SubsidiaryList getByParent(string entCode, string parentCode) throws MIceSubsidiaryServiceException;
        idempotent SubsidiaryList getByUpdatedDateRange(string entCode, MIceSubsidiarySearchParam searchParam) throws MIceSubsidiaryServiceException;
        idempotent SubsidiaryList getByDepartments(string entCode, stringList departmentCodes) throws MIceSubsidiaryServiceException;
        idempotent stringList getEntAcceptCcys(string entCode) throws MIceSubsidiaryServiceException;
        idempotent MIceSimplifiedSubsidiary getOneSimplifiedSubsidiary(string entCode, string userCode) throws MIceSubsidiaryServiceException;
    };

    ["java:getset"]
    struct MIceDepartmentDetail {
        string code;
        string parentCode;
        string heirCode;
        string name;
        string fullName;
        string businessCode;
        string principle;
        int level;
        bool enabled;
        string costCenterCode;
        string nameEn;
        string fullNameEn;
        string source;
        string sourceId;
        string departmentSetCode;
        stringList subsidiaryCodes;
        int childrenCount;
        string parentBizCode;
        stringList subsidiaryBizCodes;
    };

    ["java:type:java.util.ArrayList<MIceDepartmentDetail>"]sequence<MIceDepartmentDetail> DepartmentDetailList;

    struct MIceDepartmentSearchParam {
        string keyword;
        string cipherKeyword;
        string status;
        string startDate;
        string endDate;
        UserDutyScopes dutyScopes;
    };

    struct MIceDepartmentSearchResult {
        DepartmentList departments;
        bool hasMore;
    };

    interface DepartmentServant {
        string saveDepartment(string entCode, string userCode, MIceDepartment department, bool overrideChildrenSubsidiary) throws MIceDepartmentServiceException;
        string createDefault(string entCode, string userCode, string name) throws MIceDepartmentServiceException;
        bool deleteDepartment(string entCode, string userCode, string code) throws MIceDepartmentServiceException;
        idempotent MIceDepartment get(string entCode, string code) throws MIceDepartmentServiceException;
        idempotent MIceDepartment getOrError(string entCode, string code) throws MIceDepartmentServiceException;
        idempotent DepartmentList getAll(string entCode) throws MIceDepartmentServiceException;
        idempotent DepartmentList getByCodes(string entCode, stringList codes) throws MIceDepartmentServiceException;
        idempotent DepartmentList getByBusinessCode(string entCode, stringList businessCode) throws MIceDepartmentServiceException;
        idempotent MIceDepartmentSearchResult search(string entCode, MIceDepartmentSearchParam searchParam, int limit) throws MIceDepartmentServiceException;
        idempotent DepartmentList getByPrinciple(string entCode, string principle) throws MIceDepartmentServiceException;
        idempotent bool hasSubsidiary(string entCode, string departmentCode, string subsidiaryCode) throws MIceDepartmentServiceException;
        idempotent DepartmentList getChildrenBySubsidiary(string entCode, string subsidiaryCode, string parentCode, string status, bool includeChildren) throws MIceDepartmentServiceException;
        idempotent DepartmentList getDescendantsBySubsidiary(string entCode, string subsidiaryCode, string parentCode, string status) throws MIceDepartmentServiceException;
        idempotent DepartmentList getDescendantsByParent(string entCode, string parentCode) throws MIceDepartmentServiceException;
        idempotent MIceDepartment getBySource(string entCode, string source, string sourceId) throws MIceDepartmentServiceException;
        idempotent DepartmentList getBySourceIds(string entCode, string source, stringList sourceIds) throws MIceDepartmentServiceException;
        idempotent DepartmentList searchByManagerCode(string entCode, string principle) throws MIceDepartmentServiceException;
        idempotent DepartmentDetailList getByUpdatedDateRange(string entCode, MIceDepartmentSearchParam searchParam) throws MIceDepartmentServiceException;
        idempotent DepartmentList getAllActive(string entCode) throws MIceDepartmentServiceException;
    };

    exception MIceRankServiceException {
        string message;
        int errorCode;
    };

    ["java:getset"]
    struct MIceRank {
        string code;
        string name;
        string comments;
        string businessCode;
        long createdAt;
    };

    ["java:type:java.util.ArrayList<MIceRank>"]
    sequence<MIceRank> Ranks;

    interface RankServant {
        string save(string entCode, string userCode, MIceRank rank) throws MIceRankServiceException;
        bool delete(string entCode, string userCode, string code) throws MIceRankServiceException;
        idempotent MIceRank get(string entCode, string code) throws MIceRankServiceException;
        idempotent Ranks getByName(string entCode, string name) throws MIceRankServiceException;
        idempotent Ranks getAll(string entCode) throws MIceRankServiceException;
        idempotent Ranks getByBusinessCode(string entCode, string businessCode) throws MIceRankServiceException;
        idempotent Ranks getByCodes(string entCode, stringList codes) throws MIceRankServiceException;
    };

    exception MIcePositionServiceException {
        string message;
        int errorCode;
    };

    ["java:getset"]
    struct MIcePosition {
        string code;
        string businessCode;
        string name;
        string comments;
        string parentCode;
        string heirCode;
        string fullName;
        int level;
        bool enabled;
    };

    ["java:type:java.util.ArrayList<MIcePosition>"]
    sequence<MIcePosition> Positions;

    class MIcePositionTree;

    ["java:type:java.util.ArrayList<MIcePositionTree>"]
    sequence<MIcePositionTree> PositionTreeNodes;

    class MIcePositionTree {
        MIcePosition position;
        PositionTreeNodes children;
    };

    ["java:getset"]
    struct MIcePositionSearchParam {
        string keyword;
    };

    ["java:getset"]
    struct MIcePositionSearchResult {
        bool hasMore;
        Positions data;
    };

    interface PositionServant {
        string save(string entCode, string userCode, MIcePosition position) throws MIcePositionServiceException;
        bool delete(string entCode, string userCode, string code) throws MIcePositionServiceException;
        idempotent Positions getByBusinessCode(string entCode, string businessCode) throws MIcePositionServiceException;
        idempotent Positions getAll(string entCode) throws MIcePositionServiceException;
        idempotent Positions getByCodes(string entCode, stringList codes) throws MIcePositionServiceException;
        idempotent MIcePosition get(string entCode, string code) throws MIcePositionServiceException;
        idempotent MIcePositionTree getAsTree(string entCode, string code) throws MIcePositionServiceException;
        idempotent MIcePositionSearchResult search(string entCode, MIcePositionSearchParam param, int limit) throws MIcePositionServiceException;
        idempotent Positions getByName(string entCode, string parentCode, string name) throws MIcePositionServiceException;
    };

    exception MIceEmployeeServiceException {
        string message;
        int errorCode;
    };

    ["java:getset"]
    struct MIceRTRoute {
        string userCode;
        string supervisor;
        string supervisorName;
        string supervisorEmployeeId;
        MIceDepartment department;
        bool cover;
    };

    ["java:type:java.util.ArrayList<MIceRTRoute>"]
    sequence<MIceRTRoute> RTRoutes;


    ["java:getset"]
    struct MIceEmployeeCustFields {
        string custField1;
        string custField2;
        string custField3;
        string custField4;
        string custField5;
        string custField6;
        string custField7;
        string custField8;
        string custField9;
        string custField10;
    };

  ["java:getset"]
    struct MIceEmployeeRoleScope {
        MIceDepartment department;
        MIceSubsidiary subsidiary;
    };

    ["java:type:java.util.ArrayList<MIceEmployeeRoleScope>"]sequence<MIceEmployeeRoleScope> EmployeeRoleScopes;


    ["java:getset"]
    struct MIceEmployeeRole {
        string role;
        string visibility;
        string comments;
        EmployeeRoleScopes roleScopes;
    };
    ["java:type:java.util.ArrayList<MIceEmployeeRole>"]sequence<MIceEmployeeRole> EmployeeRoles;

    ["java:getset"]
    struct MIceEmployee {
        string entCode;
        string code;
        string userCode;
        string employeeId;
        string name;
        string email;
        string phoneNo;
        string source;
        string sourceId;
        string status;
        long businessPrivilege;
        string defaultSubsidiaryCode;
        SubsidiaryList restrictedSubsidiaries;
        int activateRemindTimes;
        string residenceCode;
        long hireDate;
        bool active;
        string tag;
        string note;
        int noApprovalFlag;
        MIceRank rank;
        MIcePosition position;
        RTRoutes rtRoutes;
        MIceEmployeeCustFields custFields;
        EmployeeRoles roles;
        string firstName;
        string middleName;
        string lastName;
        string accountCode;
    };

    ["java:type:java.util.ArrayList<MIceEmployee>"]
    sequence<MIceEmployee> Employees;

    ["java:getset"]
    struct MIceEmployeeSearchParam {
        string department;
        stringList subsidiaries;
        string employeeStatus;
        string supervisor;
        string noSupervisor;
        string noDepartment;
        string accountStatus;
        string sentActivateMsg;
        string role;
        long businessPrivilege;
        stringList ranks;
        string noRank;
        UserDutyScopes dutyScopes;
        string keyword;
        string cipherKeyword;
        int offset;
        int limit;
        long startDate;
        long endDate;
        stringList departmentCodes;
        stringList frequentlyUserCodes;
    };

    ["java:getset"]
    struct MIceEmployeeSearchResult {
        int total;
        Employees data;
    };

    struct MIceEmployeeSaveResult {
        string code;
        MIceEmployee oldData;
        stringList oldSubsidiaryCodes;
        stringList oldDepartmentCodes;
        MIceEmployee newData;
        stringList newSubsidiaryCodes;
        stringList newDepartmentCodes;
    };

    struct MIceEmployeeSelfUpdateRequest {
        string phoneNo;
        string email;
        string defaultSubsidiaryCode;
    };

    ["java:type:java.util.ArrayList<MIceEmployeeSaveResult>"] sequence<MIceEmployeeSaveResult> BatchUpdateResult;

    struct MIceEmployeeSearchByKeywordParam {
        string employeeStatus;
        string accountStatus;
        string keyword;
        string cipherKeyword;
        string positionCode;
        UserDutyScopes dutyScopes;
        stringList frequentlyUserCodes;
    };

   ["java:getset"]
   struct MIceRole{
     string roleCode;
     string roleName;
   }
  ["java:type:java.util.ArrayList<MIceRole>"] sequence<MIceRole> Roles;

    dictionary<string,string> AssociateMap;//key:userCode value:accountCode

    interface EmployeeServant {
        MIceEmployeeSaveResult save(string entCode, string userCode, MIceEmployee employee) throws MIceEmployeeServiceException;
        idempotent MIceEmployee getByUserCode(string entCode, string userCode) throws MIceEmployeeServiceException;
        idempotent MIceEmployee getDetailByUserCode(string entCode, string userCode) throws MIceEmployeeServiceException;
        idempotent MIceEmployee getByUserCodeOrError(string entCode, string userCode) throws MIceEmployeeServiceException;
        idempotent MIceEmployee getByEmployeeId(string entCode, string employeeId) throws MIceEmployeeServiceException;
        idempotent string getEmployeeCodeByAccountCode(string entCode, string accountCode);
        idempotent MIceEmployeeSearchResult search(string entCode, MIceEmployeeSearchParam param) throws MIceEmployeeServiceException;
        idempotent MIceEmployeeSearchResult searchByKeywordAndRoleCode(string entCode,string keyword,string roleCode,int limit,int offset)throws MIceEmployeeServiceException;
        MIceEmployeeSaveResult selfUpdate(string entCode, string userCode, MIceEmployeeSelfUpdateRequest request) throws MIceEmployeeServiceException;
        idempotent Employees getByUserCodes(string entCode, stringList userCodes) throws MIceEmployeeServiceException;
        idempotent Employees getByRole(string entCode, string role) throws MIceEmployeeServiceException;
        idempotent Employees getByPhoneOrEmail(string entCode, string phoneOrEmail) throws MIceEmployeeServiceException;
        idempotent MIceEmployee getBySource(string entCode, string source, string sourceId) throws MIceEmployeeServiceException;
        BatchUpdateResult batchUpdateReportTo(string entCode, string operator, stringList userCodes, RTRoutes rtRoutes) throws MIceEmployeeServiceException;
        BatchUpdateResult batchUpdateDefaultSubsidiary(string entCode, string operator, stringList userCodes, string defaultSubsidiaryCode) throws MIceEmployeeServiceException;
        BatchUpdateResult batchUpdateRole(string entCode, string operator, stringList userCodes, EmployeeRoles roles) throws MIceEmployeeServiceException;
        BatchUpdateResult batchUpdateRank(string entCode, string operator, stringList userCodes, string rank) throws MIceEmployeeServiceException;
        BatchUpdateResult batchUpdateBusinessPrivilege(string entCode, string operator, stringList userCodes, long businessPrivilege) throws MIceEmployeeServiceException;
        BatchUpdateResult batchUpdatePosition(string entCode, string operator, stringList userCodes, string positionCode) throws MIceEmployeeServiceException;
        BatchUpdateResult batchUpdateStatus(string entCode, string operator, stringList userCodes, string status) throws MIceEmployeeServiceException;
        BatchUpdateResult batchUpdateResidence(string entCode, string operator, stringList userCodes, string residenceCode) throws MIceEmployeeServiceException;
        int incrementRemindTime(string entCode, string operator, string userCode) throws MIceEmployeeServiceException;
        idempotent int countEnableEmployees(string entCode) throws MIceEmployeeServiceException;
        idempotent int countEnableEmployeesByUserCodes(string entCode, stringList userCodes) throws MIceEmployeeServiceException;
        idempotent Employees getByOnlyUserCode(string userCode) throws MIceEmployeeServiceException;
        idempotent Employees getByOnlyAccountCode(string accountCode) throws MIceEmployeeServiceException;
        idempotent Employees searchByKeyword(string entCode, MIceEmployeeSearchByKeywordParam param, int limit) throws MIceEmployeeServiceException;
        idempotent Employees getByEmployeeIds(string entCode, stringList employeeIds) throws MIceEmployeeServiceException;
        idempotent stringList getSupervisors(string entCode, string userCode) throws MIceEmployeeServiceException;
        bool updateEmployeeAccountStatus(string userCode, string accountStatus) throws MIceEmployeeServiceException;
        idempotent Employees getByRoleScope(string entCode, string role, stringList subsidiaries, stringList departmentCodes) throws MIceEmployeeServiceException;
        idempotent stringList getSubsidiaryCodes(string entCode, string userCode) throws MIceEmployeeServiceException;
        idempotent Employees getByEmployeeIdOrUpdatedAt(string entCode, string userCode, MIceEmployeeSearchParam param) throws MIceEmployeeServiceException;
        idempotent Employees getByRoles(string entCode, stringList roles) throws MIceEmployeeServiceException;
        idempotent stringList listUserCodeByRankCode(string entCode, string rankCode) throws MIceEmployeeServiceException;
        idempotent stringList getSourceId(string entCode, stringList userCodes) throws MIceEmployeeServiceException;
        MIceEmployeeSaveResult saveEmployeeRoles(string entCode, string operatorUserCode, string employeeUserCode, EmployeeRoles employeeRoles) throws MIceEmployeeServiceException;
        idempotent int countEnableAndActiveEmployees(string entCode)throws MIceEmployeeServiceException;
        idempotent Roles getAllRole()throws MIceEmployeeServiceException;
        idempotent stringIntMap countEmployees(stringList entCodes,bool onlyEnabled, bool onlyActive)throws MIceEmployeeServiceException;
        MIceEmployee updateEmployeeAccountCode(string entCode, string userCode, string accountCode) throws MIceEmployeeServiceException;
        Employees batchUpdateEmployeeAccountCode(AssociateMap associateMap,string entCode) throws MIceEmployeeServiceException;
    };

    exception MIceUserGroupServiceException {
        string message;
        int errorCode;
    };

    ["java:getset"]
    struct MIceUserGroupAssignee{
        string userCode;
        string userName;
        string employeeId;
    };

    ["java:type:java.util.ArrayList<MIceUserGroupAssignee>"]
    sequence<MIceUserGroupAssignee> MIceUserGroupAssigneeSeq;

    ["java:getset"]
    class MIceSimpleSubUserGroup{
        string userGroupCode;
        string code;
        string name;
        string nameEn;
        string conditionDesc;
        bool isDefault;
        string businessCode;
    };

   ["java:type:java.util.ArrayList<MIceSimpleSubUserGroup>"]
    sequence<MIceSimpleSubUserGroup> MIceSimpleSubUserGroupSeq;

    ["java:getset"]
    class MIceSubUserGroup extends MIceSimpleSubUserGroup{
        MIceUserGroupAssigneeSeq assignees;
    }
    ["java:type:java.util.ArrayList<MIceSubUserGroup>"]
     sequence<MIceSubUserGroup> MIceSubUserGroupSeq;


   ["java:getset"]
   class MIceSimpleUserGroup{
     string entCode;
     string code;
     string name;
     string nameEn;
     string businessCode;
     string type;
   };

   ["java:type:java.util.ArrayList<MIceSimpleUserGroup>"]
    sequence<MIceSimpleUserGroup> MIceSimpleUserGroupSeq;

    ["java:getset"]
    class MIceUserGroup extends MIceSimpleUserGroup{
        MIceSubUserGroupSeq subUserGroups;
    };

    ["java:type:java.util.ArrayList<MIceUserGroup>"]
     sequence<MIceUserGroup> MIceUserGroupSeq;

     ["java:getset"]
     struct MIceUserGroupSearchParam{
       string businessCode;
       string userGroupName;
       string userCode;
     }

  interface UserGroupServant{
    MIceUserGroup saveOrUpdateUserGrooup(string entCode, string userCode, MIceUserGroup userGroup) throws MIceUserGroupServiceException;
    void deleteUserGroup(string entCode, string userCode, string userGroupCode)throws MIceUserGroupServiceException;
    idempotent MIceSimpleUserGroupSeq listAllApprovalUserGroups(string entCode)throws MIceUserGroupServiceException;
    idempotent MIceSimpleUserGroup getUserGroupByBusinessCode(string entCode, string businessCode)throws MIceUserGroupServiceException;
    idempotent MIceUserGroup getUserGroupDetailByBusinessCode(string entCode, string businessCode)throws MIceUserGroupServiceException;
    idempotent MIceSimpleUserGroupSeq searchUserGroup(string entCode, string type, string query)throws MIceUserGroupServiceException;
    idempotent MIceUserGroup getUserGroupDetailByCode(string entCode, string userGroupCode)throws MIceUserGroupServiceException;
    idempotent MIceSimpleUserGroup getUserGroupByCode(string entCode, string userGroupCode)throws MIceUserGroupServiceException;
    idempotent MIceSimpleUserGroupSeq getUserGroupByCodes(string entCode, stringList userGroupCodes)throws MIceUserGroupServiceException;
    idempotent MIceUserGroupSeq searchUserGroupDetailByParam(string entCode, MIceUserGroupSearchParam searchParam)throws MIceUserGroupServiceException;
    idempotent MIceSimpleUserGroupSeq getUserGroupByUserCodes(string entCode, stringList userCodes)throws MIceUserGroupServiceException;

    MIceSubUserGroup saveOrUpdateSubUserGroup(string entCode, string userCode, string userGroupCode,MIceSubUserGroup subUserGroup)throws MIceUserGroupServiceException;
    idempotent stringList getSubUserGroupCodesByUserCode(string entCode, string userCode)throws MIceUserGroupServiceException;
    idempotent stringList getSubUserGroupCodesByUser(string entCode, string type, string userCode)throws MIceUserGroupServiceException;
    idempotent MIceSimpleSubUserGroup getSubUserGroupBySubUserGroupCode(string entCode, string subUserGroupCode)throws MIceUserGroupServiceException;
    idempotent MIceSimpleSubUserGroup getSubUserGroupBySubUserGroupBusinessCode(string entCode, string userGroupCode,string subUserGroupBusinessCode)throws MIceUserGroupServiceException;
    idempotent MIceSimpleSubUserGroup getDefaultSubUserGroup(string entCode, string userGroupCode)throws MIceUserGroupServiceException;
    idempotent MIceSimpleSubUserGroupSeq listSubUserGroupByUserGroupCode(string entCode,string userGroupCode)throws MIceUserGroupServiceException;
    void batchDeleteSubUserGroups(string entCode,string userCode,stringList subUserGroupCodes)throws MIceUserGroupServiceException;

    void saveAssignees(string entCode, string userCode, string subUserGroupCode,MIceUserGroupAssigneeSeq assignees)throws MIceUserGroupServiceException;
    void deleteAssigneesByUserCodes(string entCode, string subUserGroupCode,stringList userCodes)throws MIceUserGroupServiceException;
    idempotent MIceUserGroupAssigneeSeq getAssigneesByUserGroupCode(string entCode, string userGroupCode)throws MIceUserGroupServiceException;
    idempotent MIceUserGroupAssigneeSeq getAssigneesByUserGroupCodes(string entCode, stringList userGroupCodes)throws MIceUserGroupServiceException;
    idempotent MIceUserGroupAssigneeSeq getAssigneeBySubUserGroupCode(string entCode, string subUserGroupCode)throws MIceUserGroupServiceException;
  };

};
