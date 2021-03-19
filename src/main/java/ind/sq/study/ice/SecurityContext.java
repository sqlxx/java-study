package ind.sq.study.ice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sun Qin
 * @since 2021/1/4
 */
public class SecurityContext {
    public static Map<String, String> getContext() {
       var map = new HashMap<String, String>();
       map.put("test1", "ttes2");

       return map;
    }
}
