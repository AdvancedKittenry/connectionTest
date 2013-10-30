<%@tag description="fancy tables" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="data" type="connectiontest.db.ResultContainer" %>
<c:choose>
    <c:when test="${data.size > 0}">
        <table class="table table-striped table-bordered">
            <tr>
            <c:forEach var="name" items="${data.columns}">
                <th>${name}</th>
            </c:forEach>
            </tr>
            <c:forEach var="row" items="${data.rows}">
                <tr>
                <c:forEach var="value" items="${row}">
                    <td>${value}</td>
                </c:forEach>
                </tr>
            </c:forEach>
        </table>
    </c:when>
    <c:otherwise>
        <p>no data</p>
    </c:otherwise>
</c:choose>

<!--
function prettyprint($data) {

  echo '<table class="table table-striped table-bordered">';
  echo '<tr>',implode('', array_map(function($t) {return "<th>$t</th>";}, array_keys((array)array_shift(array_values($data))))),'</tr>';
  foreach($data as $datum) {
    echo '<tr>',implode('', array_map(function($t) {return "<td>$t</td>";}, (array)$datum)),'</tr>';
  }
  echo '</table>';
} -->