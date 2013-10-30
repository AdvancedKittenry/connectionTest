<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<t:base>
    <h1>Projektin tietokantataulut</h1>
    <c:forEach var="table" items="${tables}">
        <div class='panel panel-default'><div class='panel-body'>
            <h2>${table.name}</h2>
            <t:table data="${table.columns}"></t:table>
            
            <button type='button' class='btn-link expandable collapsed' data-toggle='collapse' data-target='#expandable_${table.name}'>
                Yhteensä ${table.rowCount} riviä:
            </button>
            <div id='expandable_${table.name}' class='collapse'>
                <t:table data="${table.rows}"></t:table>
            </div>
        </div></div>
    </c:forEach>
</t:base>