<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@ tag import="org.apache.commons.beanutils.*" %>
<%@ tag import="com.autobizlogic.abl.businesslogicengine.*" %>
<%@ tag import="com.autobizlogic.abl.businesslogicengine.ruleexec.*" %>
<%@ tag import="com.autobizlogic.abl.event.*" %>
<%@ tag import="autobizlogic.demo.buslogicdemospring.DemoEventListener" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="orderNumber" type="java.lang.Long" %>
<div style="border: 1px solid #888888; padding: 5px;" id="ChangeSummaryDiv">
	<div align="center"><h4>What just happened?</h4></div>
	<table style="border-spacing: 0px; padding: 3px; border: 1px solid #777777; margin: 5px;">

<%
if (DemoEventListener.getInstance() == null || DemoEventListener.getInstance().events.size() == 0) {
%>
		<tr><td style="font-style: italic;">No events have occurred (yet)</td></tr>
<%
}
else {
for (LogicEvent event : DemoEventListener.getInstance().events) {
	String eventType = "&lt;unknown&gt;";
	String desc = event.toString();
	switch(event.getEventType()) {
		case AFTER_AGGREGATE: {
			eventType = "Sum";
			LogicAfterAggregateEvent laae = (LogicAfterAggregateEvent)event;
			AbstractAggregateRule rule = laae.getAggregateRule();
			Object oldValue = laae.getOldValue();
			if (oldValue == null)
				oldValue = "<null>";
			String aggregateAttribName = rule.getBeanAttributeName();
			BeanMap beanMap = new BeanMap(laae.getPersistentBean());
			Object newValue = beanMap.get(aggregateAttribName);
			if (newValue == null)
				newValue = "<null>";
			if (rule instanceof CountRule)
				eventType = "Count";
			desc = rule.getLogicMethodName() + " for " + rule.getLogicGroup().getBeanClassName() +
				", old value: " + oldValue + ", new value: " + newValue;
			}
			break;
		case AFTER_FORMULA: {
			eventType = "Formula";
			LogicAfterFormulaEvent lafe = (LogicAfterFormulaEvent)event;
			FormulaRule formula = lafe.getFormulaRule();
			Object oldValue = lafe.getOldValue();
			if (oldValue == null)
				oldValue = "&lt;null>";
			String formulaAttribName = formula.getBeanAttributeName();
			BeanMap beanMap = new BeanMap(lafe.getPersistentBean());
			Object newValue = beanMap.get(formulaAttribName);
			if (newValue == null)
				newValue = "&lt;null>";
			desc = formula.getLogicMethodName() + " for " + formula.getLogicGroup().getBeanClassName() +
				", old value: " + oldValue + ", new value: " + newValue;
			}
			break;
		case BEFORE_ACTION:
			eventType = "Before action";
			break;
		case AFTER_ACTION:
			eventType = "After action";
			break;
		case AFTER_CONSTRAINT:
			eventType = "Constraint";
			LogicAfterConstraintEvent lace = (LogicAfterConstraintEvent)event;
			ConstraintRule constraint = lace.getConstraintRule();
			ConstraintFailure failure = lace.getFailure();
			desc = constraint.getLogicMethodName() + " for " + constraint.getLogicGroup().getBeanClassName();
			if (failure == null)
				desc += ", OK";
			else
				desc += ", failed: " + failure.getConstraintMessage();
			
			break;
		case AFTER_PARENT_COPY:
			eventType = "Parent copy";
			LogicAfterParentCopyEvent lapce = (LogicAfterParentCopyEvent)event;
			ParentCopyRule rule = lapce.getParentCopyRule();
			BeanMap beanMap = new BeanMap(lapce.getPersistentBean());
			Object copyValue = beanMap.get(rule.getChildAttributeName());
			if (copyValue == null)
				copyValue = "&lt;null>";
			desc = "From " + rule.getRoleName() + "." + rule.getParentAttributeName() + " to " + rule.getChildAttributeName() + ", value " + copyValue;
			
			break;
		case LOGIC_RUNNER: 
			eventType = "Logic event";
			LogicRunnerEvent lre = (LogicRunnerEvent)event;
			if (lre.getLogicRunnerEventType() == LogicRunnerEvent.LogicRunnerEventType.END)
				continue;
			desc = event.getTitle();
			
			break;
		case BEFORE_COMMIT:
			continue;
			//eventType = "Before commit";
			//break;
		case AFTER_COMMIT:
			continue;
			//eventType = "After commit";
			//break;
		default:
			throw new RuntimeException("Unknown event type:" + event.getEventType());
	}
%>
	<tr>
		<td style='border: 1px solid #777777;'><%= eventType %></td>
		<td style='border: 1px solid #777777;'><%= desc %></td>
	</tr>
<%
}

DemoEventListener.getInstance().resetEvents();
}
%>
	</table>
</div>
