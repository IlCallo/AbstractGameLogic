package blazing.tears.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static blazing.tears.action.Action.*;

// TODO: is it possible to do this by setting all main.role classes with static content?
public class ActionProvider {
    static private final Map<Action, BaseAction> sActions;

    static {
        Map<Action, BaseAction> aMap = new HashMap<>();
        aMap.put(PROTECT, new ProtectAction());
        aMap.put(CALM, new CalmAction());
        aMap.put(DEMOLISH, new DemolishAction());
        aMap.put(ASSASSINATE, new AssassinateAction());
        aMap.put(BUILD, new BuildAction());
        aMap.put(PANIC, new PanicAction());
        aMap.put(COLLECT, new CollectAction());
        sActions = Collections.unmodifiableMap(aMap);
    }

    static public BaseAction getAction(Action name) {
        return sActions.get(name);
    }

    static public class ProtectAction extends BaseAction {

        public ProtectAction() {
            super(PROTECT.name(), "Every allied unit within 10 meters by you cannot be assassinated");
        }

        @Override
        public void doAction() {

        }
    }

    static public class CalmAction extends BaseAction {

        public CalmAction() {
            super(CALM.name(), "");
        }

        @Override
        public void doAction() {

        }
    }

    static public class DemolishAction extends BaseAction {

        public DemolishAction() {
            super(DEMOLISH.name(), "");
        }

        @Override
        public void doAction() {

        }
    }

    static public class AssassinateAction extends BaseAction {

        public AssassinateAction() {
            super(ASSASSINATE.name(), "");
        }

        @Override
        public void doAction() {

        }
    }

    static public class BuildAction extends BaseAction {

        public BuildAction() {
            super(BUILD.name(), "");
        }

        @Override
        public void doAction() {

        }
    }

    static public class PanicAction extends BaseAction {

        public PanicAction() {
            super(PANIC.name(), "");
        }

        @Override
        public void doAction() {

        }
    }

    static public class CollectAction extends BaseAction {

        public CollectAction() {
            super(COLLECT.name(), "");
        }

        @Override
        public void doAction() {

        }
    }
}