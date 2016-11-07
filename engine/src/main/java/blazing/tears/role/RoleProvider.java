package blazing.tears.role;

import blazing.tears.action.ActionProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static blazing.tears.action.Action.*;
import static blazing.tears.role.Role.*;

// TODO: is it possible to do this by setting all role classes with static content?
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
        aMap.put(UNSPECIFIED, null);
        sRoles = Collections.unmodifiableMap(aMap);
    }

    static public BaseRole getRole(Role name) {
        return sRoles.get(name);
    }

    static public Map<Role, Integer> getInitialRolePool() {
        Map<Role, Integer> rolePool = new HashMap<>();
        rolePool.put(COP, 2);
        rolePool.put(ASSASSIN, 2);
        rolePool.put(BUILDER, 2);
        rolePool.put(COLLECTOR, 999);
        rolePool.put(UNTOUCHABLE, 0);
        rolePool.put(MULTITASKING, 0);
        return rolePool;
    }

    public static class CopRole extends BaseRole {
        public CopRole() {
            super(COP.name(), Arrays.asList(ActionProvider.getAction(PROTECT), ActionProvider.getAction(CALM)));
        }
    }

    public static class AssassinRole extends BaseRole {
        public AssassinRole() {
            super(ASSASSIN.name(), Arrays.asList(ActionProvider.getAction(ASSASSINATE), ActionProvider.getAction(DEMOLISH)));
        }
    }

    public static class BuilderRole extends BaseRole {
        public BuilderRole() {
            super(BUILDER.name(), Arrays.asList(ActionProvider.getAction(BUILD), ActionProvider.getAction(PANIC)));
        }
    }

    public static class CollectorRole extends BaseRole {
        public CollectorRole() {
            super(COLLECTOR.name(), Collections.singletonList(ActionProvider.getAction(COLLECT)));
        }
    }

    public static class UntouchableRole extends BaseRole {
        public UntouchableRole() {
            super(UNTOUCHABLE.name());
        }
    }

    public static class MultitaskingRole extends BaseRole {
        public MultitaskingRole() {
            super(MULTITASKING.name());
        }
    }
}