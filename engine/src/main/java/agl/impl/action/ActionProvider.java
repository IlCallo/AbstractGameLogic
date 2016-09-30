package agl.impl.action;

import agl.core.aglAction;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

// TODO: is it possible to do this by setting all role classes with static content?
public class ActionProvider {

    static public final String PROTECT_ACTION = "protect";
    static public final String CALM_ACTION = "calm";
    static public final String DEMOLISH_ACTION = "demolish";
    static public final String ASSASSINATE_ACTION = "assassinate";
    static public final String BUILD_ACTION = "build";
    static public final String PANIC_ACTION = "panic";
    static public final String COLLECT_ACTION = "collect";

    static private final Map<String, aglAction> sActions = ImmutableMap.<String, aglAction>builder()
            .put(PROTECT_ACTION, new ProtectAction())
            .put(CALM_ACTION, new CalmAction())
            .put(DEMOLISH_ACTION, new DemolishAction())
            .put(ASSASSINATE_ACTION, new AssassinateAction())
            .put(BUILD_ACTION, new BuildAction())
            .put(PANIC_ACTION, new PanicAction())
            .put(COLLECT_ACTION, new CollectAction())
            .build();

    static public aglAction getAction(String name) {
        return sActions.get(name);
    }

    static public class ProtectAction extends aglAction {

        public ProtectAction() {
            super(PROTECT_ACTION, "Every allied unit within 10 meters by you cannot be assassinated");
        }

        @Override
        public void doAction() {

        }
    }

    static public class CalmAction extends aglAction {

        public CalmAction() {
            super(CALM_ACTION, "");
        }

        @Override
        public void doAction() {

        }
    }

    static public class DemolishAction extends aglAction {

        public DemolishAction() {
            super(DEMOLISH_ACTION, "");
        }

        @Override
        public void doAction() {

        }
    }

    static public class AssassinateAction extends aglAction {

        public AssassinateAction() {
            super(ASSASSINATE_ACTION, "");
        }

        @Override
        public void doAction() {

        }
    }

    static public class BuildAction extends aglAction {

        public BuildAction() {
            super(BUILD_ACTION, "");
        }

        @Override
        public void doAction() {

        }
    }

    static public class PanicAction extends aglAction {

        public PanicAction() {
            super(PANIC_ACTION, "");
        }

        @Override
        public void doAction() {

        }
    }

    static public class CollectAction extends aglAction {

        public CollectAction() {
            super(COLLECT_ACTION, "");
        }

        @Override
        public void doAction() {

        }
    }
}