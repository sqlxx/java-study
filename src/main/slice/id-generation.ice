[["java:package:com.maycur.ice"]]
module idgeneration {

    sequence<string> UniqueIds;
    class MIceTestParam {
        string departmentCode;
        string subsidiaryCode;
        optional(1) string userCode;

    };

    interface IdGenerationService {
        idempotent string getUniqueId();
        idempotent UniqueIds getBatchUniqueIds();
        idempotent void testIt(MIceTestParam param);

    };

};
