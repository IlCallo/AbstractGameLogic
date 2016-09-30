package agl.impl.role;

import agl.core.aglRole;
import agl.impl.action.ActionProvider;
import com.google.common.collect.ImmutableMap;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

// TODO: is it possible to do this by setting all role classes with static content?
public class RoleProvider {

    static public final String COP_ROLE = "cop";
    static public final String ASSASSIN_ROLE = "assassin";
    static public final String BUILDER_ROLE = "builder";
    static public final String COLLECTOR_ROLE = "collector";
    static public final String UNTOUCHABLE_ROLE = "untouchable";
    static public final String MULTITASKING_ROLE = "multitasking";

    static private final Map<String, aglRole> sRoles = ImmutableMap.<String, aglRole>builder()
            .put(COP_ROLE, new CopRole())
            .put(ASSASSIN_ROLE, new AssassinRole())
            .put(BUILDER_ROLE, new BuilderRole())
            .put(COLLECTOR_ROLE, new CollectorRole())
            .put(UNTOUCHABLE_ROLE, new UntouchableRole())
            .put(MULTITASKING_ROLE, new MultitaskingRole())
            .build();

    static public aglRole getRole(String name) {
        return sRoles.get(name);
    }

    static public Map<String, Integer> getInitialRolePool() {
        Map<String, Integer> rolePool = new HashMap<>();
        rolePool.put(COP_ROLE, 2);
        rolePool.put(ASSASSIN_ROLE, 2);
        rolePool.put(BUILDER_ROLE, 2);
        rolePool.put(COLLECTOR_ROLE, 1000);
        rolePool.put(UNTOUCHABLE_ROLE, 0);
        rolePool.put(MULTITASKING_ROLE, 0);
        return rolePool;
    }

    public static class CopRole extends aglRole {
        public CopRole() {
            super(COP_ROLE, Arrays.asList(ActionProvider.getAction(ActionProvider.PROTECT_ACTION),
                    ActionProvider.getAction(ActionProvider.CALM_ACTION)));
        }
    }

    public static class AssassinRole extends aglRole {
        public AssassinRole() {
            super(ASSASSIN_ROLE, Arrays.asList(ActionProvider.getAction(ActionProvider.ASSASSINATE_ACTION),
                    ActionProvider.getAction(ActionProvider.DEMOLISH_ACTION)));
        }
    }

    public static class BuilderRole extends aglRole {
        public BuilderRole() {
            super(BUILDER_ROLE, Arrays.asList(ActionProvider.getAction(ActionProvider.BUILD_ACTION),
                    ActionProvider.getAction(ActionProvider.PANIC_ACTION)));
        }
    }

    public static class CollectorRole extends aglRole {
        public CollectorRole() {
            super(COLLECTOR_ROLE, Collections.singletonList(ActionProvider.getAction(ActionProvider.COLLECT_ACTION)));
        }
    }

    public static class UntouchableRole extends aglRole {
        public UntouchableRole() {
            super(UNTOUCHABLE_ROLE, Collections.emptyList());
        }
    }

    public static class MultitaskingRole extends aglRole {
        public MultitaskingRole() {
            super(MULTITASKING_ROLE, Collections.emptyList());
        }
    }
}