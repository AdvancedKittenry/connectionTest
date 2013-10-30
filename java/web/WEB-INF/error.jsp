<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:base>
    <h1>Virhe</h1>
    <pre>
        <c:out value="${error}"></c:out>
    </pre>
</t:base>