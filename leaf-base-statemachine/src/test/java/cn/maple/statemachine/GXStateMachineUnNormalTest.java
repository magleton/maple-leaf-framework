package cn.maple.statemachine;

import cn.maple.statemachine.builder.GXStateMachineBuilder;
import cn.maple.statemachine.builder.GXStateMachineBuilderFactory;
import cn.maple.statemachine.impl.GXDebugger;
import cn.maple.statemachine.impl.GXStateMachineException;
import org.junit.Assert;
import org.junit.Test;

public class GXStateMachineUnNormalTest {
    @Test
    public void testConditionNotMeet() {
        GXStateMachineBuilder<GXStateMachineTest.States, GXStateMachineTest.Events, GXStateMachineTest.Context> builder = GXStateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(GXStateMachineTest.States.STATE1)
                .to(GXStateMachineTest.States.STATE2)
                .on(GXStateMachineTest.Events.EVENT1)
                .when(checkConditionFalse())
                .perform(doAction());

        GXStateMachine<GXStateMachineTest.States, GXStateMachineTest.Events, GXStateMachineTest.Context> stateMachine = builder.build("NotMeetConditionMachine");
        GXStateMachineTest.States target = stateMachine.fireEvent(GXStateMachineTest.States.STATE1, GXStateMachineTest.Events.EVENT1, new GXStateMachineTest.Context());
        Assert.assertEquals(GXStateMachineTest.States.STATE1, target);
    }


    @Test(expected = GXStateMachineException.class)
    public void testDuplicatedTransition() {
        GXStateMachineBuilder<GXStateMachineTest.States, GXStateMachineTest.Events, GXStateMachineTest.Context> builder = GXStateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(GXStateMachineTest.States.STATE1)
                .to(GXStateMachineTest.States.STATE2)
                .on(GXStateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        builder.externalTransition()
                .from(GXStateMachineTest.States.STATE1)
                .to(GXStateMachineTest.States.STATE2)
                .on(GXStateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());
    }

    @Test(expected = GXStateMachineException.class)
    public void testDuplicateMachine() {
        GXStateMachineBuilder<GXStateMachineTest.States, GXStateMachineTest.Events, GXStateMachineTest.Context> builder = GXStateMachineBuilderFactory.create();
        builder.externalTransition()
                .from(GXStateMachineTest.States.STATE1)
                .to(GXStateMachineTest.States.STATE2)
                .on(GXStateMachineTest.Events.EVENT1)
                .when(checkCondition())
                .perform(doAction());

        builder.build("DuplicatedMachine");
        builder.build("DuplicatedMachine");
    }

    private GXCondition<GXStateMachineTest.Context> checkCondition() {
        return (ctx) -> {
            return true;
        };
    }

    private GXCondition<GXStateMachineTest.Context> checkConditionFalse() {
        return (ctx) -> {
            return false;
        };
    }

    private GXAction<GXStateMachineTest.States, GXStateMachineTest.Events, GXStateMachineTest.Context> doAction() {
        GXDebugger.enableDebug();
        return (from, to, event, ctx) -> {
            GXDebugger.debug(ctx.operator + " is operating " + ctx.entityId + "from:" + from + " to:" + to + " on:" + event);
        };
    }
}
