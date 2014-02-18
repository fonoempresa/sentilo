<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/header.jsp"%>
<%@include file="/WEB-INF/jsp/common/taglibs.jsp"%>

<c:if test="${mode == 'edit' }">
	<spring:url value="/admin/sensor/${sensor.id}/edit" var="actionURL" />
	<spring:message code="sensor.edit.title" var="pageTitle" />
</c:if>
<c:if test="${mode == 'create' }">
	<spring:url value="/admin/component/search/json" var="componentSearchURL" />
	<spring:url value="/admin/sensor/create" var="actionURL" />
	<spring:message code="sensor.new.title" var="pageTitle" />
	<spring:message code="select.empty" var="emptySelectMessage" />
</c:if>

<c:if test="${empty providerId}">
	<spring:url value="/admin/sensor/list" var="backURL" />
</c:if>
<c:if test="${not empty providerId}">
	<spring:url value="/admin/provider/${providerId}/detail" var="backURL" />
</c:if>

<c:set var="editMode" value="${mode == 'edit' }" />

<%@include file="/WEB-INF/jsp/common/include_script_datepickers.jsp"%>
<script type="text/javascript">
$(document).ready(function() {
	makeDateRangePicker('#validTime');
	makeDatePicker('#installationDateDatePicker');
	<c:if test="${not empty providerId && not editMode}">
		populateComponents();
	</c:if>
	
});

function populateComponents() {
	var providerId = $('#providerId').val();
	var selectComponentId = $('#componentId');	
	if (providerId) {
		emptyComponentSelect();
		jsonGET('${componentSearchURL}?providerId=' + providerId, [], function(data) {
			for(var i=0; i<data.length; i++) {
				addComponentToSelect(selectComponentId, data[i]);
			}			
		});
	} 
};

function addComponentToSelect(select, component) {	
	select.append('<option value="' + component.id + '">' + component.name +'</option>');		
};

function emptyComponentSelect() {
	$('#componentId')
	    .find('option')
	    .remove()
	    .end()
	    .append('<option value="">${emptySelectMessage}</option>')
	    .val('');
};

</script>

<div class="container-fluid">
	<div class="content">
		<div class="row-fluid">
			<div class="span3">
				<%@include file="/WEB-INF/jsp/common/include_sidebar.jsp"%>
			</div>
			<div class="span9">
				<div class="row-fluid">
					<div class="span12">

						<%@include file="/WEB-INF/jsp/common/include_background_logo.jsp"%>
						<%@include file="/WEB-INF/jsp/common/messages.jsp"%>

						<h1 class="lead">
							${pageTitle}<br />
						</h1>

						<form:form method="post" modelAttribute="sensor" action="${actionURL}" class="form-horizontal" autocomplete="off">
							<fieldset>
								<div class="tabbable">
									<ul class="nav nav-tabs">
										<li class="active"><a href="#tab1" data-toggle="tab"><spring:message code="sensor.detail.title" /> </a>
										</li>
										<li><a href="#tab2" data-toggle="tab"><spring:message code="sensor.additionalInfo" /> </a></li>
									</ul>
									<c:if test="${editMode}">
										<form:hidden path="id" />
										<form:hidden path="createdAt" />
									</c:if>
									<div class="tab-content">
										<div class="tab-pane active" id="tab1">
											<div class="control-group">
												<form:label path="sensorId" class="control-label">
													<spring:message code="sensor.sensorId" />
												</form:label>

												<div class="controls">
													<form:input path="sensorId" readonly="${editMode}" />
													<form:errors path="sensorId" cssClass="text-error" htmlEscape="false" />
												</div>
											</div>

											<c:if test="${empty providerId}">
												<div class="control-group">
													<form:label path="providerId" class="control-label">
														<spring:message code="sensor.providerId" />
													</form:label>
													<div class="controls">
														<form:select path="providerId" id="providerId" onchange="populateComponents();" disabled="${editMode}">
															<form:option value="">${emptySelectMessage}</form:option>
															<form:options items="${providers}" itemValue="id" itemLabel="name" />
														</form:select>
														<c:if test="${editMode}">
															<form:hidden path="providerId" />
														</c:if>
														<form:errors path="providerId" cssClass="text-error" htmlEscape="false" />
													</div>
												</div>
												<input type="hidden" name="origin" value="sensor" />
											</c:if>
											<c:if test="${not empty providerId}">
												<input type="hidden" name="providerId" id="providerId" value="${providerId}" />
												<input type="hidden" name="origin" value="provider" />
											</c:if>

											<div class="control-group">
												<form:label path="description" class="control-label">
													<spring:message code="sensor.description" />
												</form:label>
												<div class="controls">
													<form:textarea path="description" />
													<form:errors path="description" cssClass="text-error" htmlEscape="false" />
												</div>
											</div>
											<div class="control-group">
												<form:label path="componentId" class="control-label">
													<spring:message code="sensor.componentId" />
												</form:label>
												<div class="controls">
													<form:select path="componentId" cssClass="input-large" disabled="${editMode}">
														<form:option value="">${emptySelectMessage}</form:option>
														<c:if test="${editMode}">
															<form:options items="${components}" itemLabel="name" itemValue="id" />
														</c:if>
													</form:select>
													<c:if test="${editMode}">
														<form:hidden path="componentId" />
													</c:if>
													<form:errors path="componentId" cssClass="text-error" htmlEscape="false" />
												</div>
											</div>

											<div class="control-group">
												<form:label path="publicAccess" class="control-label">
													<spring:message code="sensor.publicAccess" />
												</form:label>
												<div class="controls">
													<form:checkbox path="publicAccess" />
													<form:errors path="publicAccess" cssClass="text-error" htmlEscape="false" />
												</div>
											</div>
											<div class="control-group">
												<form:label path="type" class="control-label">
													<spring:message code="sensor.type" />
												</form:label>
												<div class="controls">
													<form:select path="type">
														<form:options items="${sensorTypes}" itemValue="id" itemLabel="name" />
													</form:select>
													<form:errors path="type" cssClass="text-error" htmlEscape="false" />
												</div>
											</div>
											<div class="control-group">
												<form:label path="dataType" class="control-label">
													<spring:message code="sensor.dataType" />
												</form:label>
												<div class="controls">
													<form:select path="dataType">
														<c:forEach items="${sensorDataTypes}" var="dataType">
															<form:option value="${dataType}">
																<spring:message code="sensor.dataType.${dataType}" />
															</form:option>
														</c:forEach>
													</form:select>
													<form:errors path="dataType" cssClass="text-error" htmlEscape="false" />
												</div>
											</div>
											<div class="control-group">
												<form:label path="unit" class="control-label">
													<spring:message code="sensor.unit" />
												</form:label>
												<div class="controls">
													<form:input path="unit" />
													<form:errors path="unit" cssClass="text-error" htmlEscape="false" />
												</div>
											</div>
											<div class="control-group">
												<form:label path="validTime" class="control-label">
													<spring:message code="sensor.validTime" />
												</form:label>
												<div class="controls">
													<div class="input-append">
														<form:input type="text" path="validTime" id="validTime" />
														<span class="add-on"><i class="icon-calendar"></i> </span>
													</div>
													<form:errors path="validTime" cssClass="text-error" htmlEscape="false" />
												</div>
											</div>
											<div class="control-group">
												<form:label path="metaData" class="control-label">
													<spring:message code="sensor.metaData" />
												</form:label>
												<div class="controls">
													<form:input path="metaData" />
													<form:errors path="metaData" cssClass="text-error" htmlEscape="false" />
												</div>
											</div>
											<%@include file="/WEB-INF/jsp/common/include_input_tags.jsp"%>
										</div>
										<div class="tab-pane" id="tab2">
											<%@include file="include_sensor_additional_info.jsp"%>
										</div>
									</div>
								</div>
								<div class="control-group">
									<div class="controls">
										<a href="${backURL}" class="btn"> <spring:message code="button.back" /> </a> <a href="#"
											onclick="$('form#sensor').submit();" class="btn btn-success"> <spring:message code="button.save" /> </a>
									</div>
								</div>
							</fieldset>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<%@include file="/WEB-INF/jsp/common/footer.jsp"%>