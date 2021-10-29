package ind.sq.study.script;

import org.mvel2.MVEL;

import java.security.Permission;
import java.time.LocalDateTime;
import java.util.HashMap;

public class MvelTest {

    public String testMethod(String input) {
        System.out.println("Java test Method with input: " + input);
        return LocalDateTime.now().toString();

    }

    public static void main(String[] args) {
        String mvelScript = "System.out.println(\"I'm in mvel script\"); var result = obj.testMethod('Beily'); System.exit(1);System.out.println(\"The result is \" + result); return result;";

        var securityManager = new SecurityManager(){
            @Override
            public void checkPermission(Permission perm) {
                if (perm.getName().startsWith("exitVM")) {
                    throw new SecurityException("Exit trap");
                }
            }

            @Override
            public void checkExit(int status) {
                super.checkExit(status);
            }
        };

        System.setSecurityManager(securityManager);
        var compileResult = MVEL.compileExpression(mvelScript);
        var input = new HashMap<>();
        input.put("obj", new MvelTest());
        var result = MVEL.executeExpression(compileResult, input);
        System.out.println("result in java: " + result);

    }
}
