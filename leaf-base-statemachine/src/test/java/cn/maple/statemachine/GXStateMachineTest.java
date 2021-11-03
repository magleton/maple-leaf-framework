package cn.maple.statemachine;

import cn.maple.statemachine.builder.GXStateMachineBuilder;
import cn.maple.statemachine.builder.GXStateMachineBuilderFactory;
import cn.maple.statemachine.impl.GXDebugger;
import org.junit.Assert;
import org.junit.Test;

public class GXStateMachineTest {
    /**
     * 机器ID
     */
    static String MACHINE_ID = "TestStateMachine";

    @Test
    public void testExternalNormal() {
        GXStateMachineBuilder<States, Events, Context> builder = GXStateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(States.STATE1)
                .to(States.STATE2)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        GXStateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID);
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
        Assert.assertEquals(States.STATE2, target);
    }

    @Test
    public void testExternalTransitionsNormal() {
        GXStateMachineBuilder<States, Events, Context> builder = GXStateMachineBuilderFactory.create();
        builder.externalTransitions()
                .fromAmong(States.STATE1, States.STATE2, States.STATE3)
                .to(States.STATE4)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        GXStateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID + "1");
        States target = stateMachine.fireEvent(States.STATE2, Events.EVENT1, new Context());
        Assert.assertEquals(States.STATE4, target);
    }

    @Test
    public void testInternalNormal() {
        GXStateMachineBuilder<States, Events, Context> builder = GXStateMachineBuilderFactory.create();
        builder.internalTransition()
                .within(States.STATE1)
                .on(Events.INTERNAL_EVENT)
                .when(checkCondition())
                .perform(doAction());
        GXStateMachine<States, Events, Context> stateMachine = builder.build(MACHINE_ID + "2");

        stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
        States target = stateMachine.fireEvent(States.STATE1, Events.INTERNAL_EVENT, new Context());
        Assert.assertEquals(States.STATE1, target);
    }

    @Test
    public void testExternalInternalNormal() {
        GXStateMachine<States, Events, Context> stateMachine = buildStateMachine("testExternalInternalNormal");

        Context context = new Context();
        States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, context);
        Assert.assertEquals(States.STATE2, target);
        target = stateMachine.fireEvent(States.STATE2, Events.INTERNAL_EVENT, context);
        Assert.assertEquals(States.STATE2, target);
        target = stateMachine.fireEvent(States.STATE2, Events.EVENT2, context);
        Assert.assertEquals(States.STATE1, target);
        target = stateMachine.fireEvent(States.STATE1, Events.EVENT3, context);
        Assert.assertEquals(States.STATE3, target);
    }

    private GXStateMachine<States, Events, Context> buildStateMachine(String machineId) {
        GXStateMachineBuilder<States, Events, Context> builder = GXStateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(States.STATE1)
                .to(States.STATE2)
                .on(Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        builder.internalTransition()
                .within(States.STATE2)
                .on(Events.INTERNAL_EVENT)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(States.STATE2)
                .to(States.STATE1)
                .on(Events.EVENT2)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(States.STATE1)
                .to(States.STATE3)
                .on(Events.EVENT3)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransitions()
                .fromAmong(States.STATE1, States.STATE2, States.STATE3)
                .to(States.STATE4)
                .on(Events.EVENT4)
                .when(checkCondition())
                .perform(doAction());

        builder.build(machineId);

        GXStateMachine<States, Events, Context> stateMachine = GXStateMachineFactory.get(machineId);
        stateMachine.showStateMachine();
        return stateMachine;
    }

    @Test
    public void testMultiThread() {
        buildStateMachine("testMultiThread");
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                GXStateMachine<States, Events, Context> stateMachine = GXStateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT1, new Context());
                Assert.assertEquals(States.STATE2, target);
            });
            thread.start();
        }

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                GXStateMachine<States, Events, Context> stateMachine = GXStateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT4, new Context());
                Assert.assertEquals(States.STATE4, target);
            });
            thread.start();
        }

        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(() -> {
                GXStateMachine<States, Events, Context> stateMachine = GXStateMachineFactory.get("testMultiThread");
                States target = stateMachine.fireEvent(States.STATE1, Events.EVENT3, new Context());
                Assert.assertEquals(States.STATE3, target);
            });
            thread.start();
        }

    }

    private GXCondition<Context> checkCondition() {
        return (ctx) -> {
            return true;
        };
    }

    private GXAction<States, Events, Context> doAction() {
        GXDebugger.enableDebug();
        return (from, to, event, ctx) -> {
            GXDebugger.debug(ctx.operator + " is operating " + ctx.entityId + " from:" + from + " to:" + to + " on:" + event);
        };
    }

    enum States {
        STATE1, STATE2, STATE3, STATE4
    }


    enum Events {
        EVENT1, EVENT2, EVENT3, EVENT4, INTERNAL_EVENT
    }

    static class Context {
        String operator = "枫叶思源";
        String entityId = "123465";
    }
}
