package blazing.tears.role;

import blazing.tears.action.ActionProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static blazing.tears.role.Role.*;

// TODO: is it possible to do this by setting all main.role classes with static content?
public class RoleProvider {
    static private final Map<Role, BaseRole> sRoles;

    static {
        Map<Role, BaseRole> aMap = new HashMap<>();
        aMap.put(COP, new CopRole());
        aMap.put(ASSASSIN, new AssassinRole());
        aMap.put(BUILDER, new BuilderRole());
        aMap.put(COLLECTOR, new CollectorRole());
        aMap.put(UNTOUCHABLE, new UntouchableRole());
        aMap.put(MULTITASKING, new MultitaskingRole());
        sRoles = Collections.unmodifiableMap(aMap);
    }

    static public BaseRole getRole(String name) {
        return sRoles.get(name);
    }

    static public Map<Role, Integer> getInitialRolePool() {
        Map<Role, Integer> rolePool = new HashMap<>();
        rolePool.put(COP, 2);
        rolePool.put(ASSASSIN, 2);
        rolePool.put(BUILDER, 2);
        rolePool.put(COLLECTOR, 1000);
        rolePool.put(UNTOUCHABLE, 0);
        rolePool.put(MULTITASKING, 0);
        return rolePool;
    }

    public static class CopRole extends BaseRole {
        public CopRole() {
            super(COP.name(), Arrays.asList(ActionProvider.getAction(ActionProvider.PROTECT_ACTION),
                    ActionProvider.getAction(ActionProvider.CALM_ACTION)));
        }
    }

    public static class AssassinRole extends BaseRole {
        public AssassinRole() {
            super(ASSASSIN.name(), Arrays.asList(ActionProvider.getAction(ActionProvider.ASSASSINATE_ACTION),
                    ActionProvider.getAction(ActionProvider.DEMOLISH_ACTION)));
        }
    }

    public static class BuilderRole extends BaseRole {
        public BuilderRole() {
            super(BUILDER.name(), Arrays.asList(ActionProvider.getAction(ActionProvider.BUILD_ACTION),
                    ActionProvider.getAction(ActionProvider.PANIC_ACTION)));
        }
    }

    public static class CollectorRole extends BaseRole {
        public CollectorRole() {
            super(COLLECTOR.name(), Collections.singletonList(ActionProvider.getAction(ActionProvider.COLLECT_ACTION)));
        }
    }

    public static class UntouchableRole extends BaseRole {
        public UntouchableRole() {
            super(UNTOUCHABLE.name(), Collections.emptyList());
        }
    }

    public static class MultitaskingRole extends BaseRole {
        public MultitaskingRole() {
            super(MULTITASKING.name(), Collections.emptyList());
        }
    }
}