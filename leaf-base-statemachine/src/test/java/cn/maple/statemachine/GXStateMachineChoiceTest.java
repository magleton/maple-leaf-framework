package cn.maple.statemachine;

import cn.hutool.core.lang.Assert;
import cn.maple.statemachine.builder.GXStateMachineBuilder;
import cn.maple.statemachine.builder.GXStateMachineBuilderFactory;
import cn.maple.statemachine.impl.GXDebugger;
import org.junit.jupiter.api.Test;

public class GXStateMachineChoiceTest {
    /**
     * 测试选择分支，针对同一个事件：EVENT1
     * if condition == "1", STATE1 --> STATE1
     * if condition == "2" , STATE1 --> STATE2
     * if condition == "3" , STATE1 --> STATE3
     */
    @Test
    public void testChoice() {
        GXStateMachineBuilder<GXStateMachineTest.States, GXStateMachineTest.Events, Context> builder = GXStateMachineBuilderFactory.create();
        builder.internalTransition()
                .within(GXStateMachineTest.States.STATE1)
                .on(GXStateMachineTest.Events.EVENT1)
                .when(checkCondition1())
                .perform(doAction());
        builder.externalTransition()
                .from(GXStateMachineTest.States.STATE1)
                .to(GXStateMachineTest.States.STATE2)
                .on(GXStateMachineTest.Events.EVENT1)
                .when(checkCondition2())
                .perform(doAction());
        builder.externalTransition()
                .from(GXStateMachineTest.States.STATE1)
                .to(GXStateMachineTest.States.STATE3)
                .on(GXStateMachineTest.Events.EVENT1)
                .when(checkCondition3())
                .perform(doAction());

        GXStateMachine<GXStateMachineTest.States, GXStateMachineTest.Events, Context> stateMachine = builder.build("ChoiceConditionMachine");
        GXStateMachineTest.States target1 = stateMachine.fireEvent(GXStateMachineTest.States.STATE1, GXStateMachineTest.Events.EVENT1, new Context("1"));
        Assert.equals(GXStateMachineTest.States.STATE1, target1);
        GXStateMachineTest.States target2 = stateMachine.fireEvent(GXStateMachineTest.States.STATE1, GXStateMachineTest.Events.EVENT1, new Context("2"));
        Assert.equals(GXStateMachineTest.States.STATE2, target2);
        GXStateMachineTest.States target3 = stateMachine.fireEvent(GXStateMachineTest.States.STATE1, GXStateMachineTest.Events.EVENT1, new Context("3"));
        Assert.equals(GXStateMachineTest.States.STATE3, target3);
    }

    private GXCondition<Context> checkCondition1() {
        return (ctx) -> "1".equals(ctx.getCondition());
    }

    private GXCondition<Context> checkCondition2() {
        return (ctx) -> "2".equals(ctx.getCondition());
    }

    private GXCondition<Context> checkCondition3() {
        return (ctx) -> "3".equals(ctx.getCondition());
    }

    private GXAction<GXStateMachineTest.States, GXStateMachineTest.Events, Context> doAction() {
        GXDebugger.enableDebug();
        return (from, to, event, ctx) -> {
            GXDebugger.debug("from:" + from + " to:" + to + " on:" + event + " condition:" + ctx.getCondition());
        };
    }

    static class Context {
        private final String condition;

        public Context(String condition) {
            this.condition = condition;
        }

        public String getCondition() {
            return condition;
        }
    }
}
