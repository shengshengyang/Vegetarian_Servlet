<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List" %>
<%@ page pageEncoding="UTF-8"%>
<jsp:useBean id="restaurant" class="bean.Restaurant" scope="request"/>
<jsp:setProperty name="restaurant" property="*"/>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>restaurantForm</title>
</head>
<style><%@include file="/../css/table.css"%></style>
<body>
<noscript>
      <iframe
        src="https://www.googletagmanager.com/ns.html?id=GTM-PGQ9WQT"
        height="0"
        width="0"
        style="display: none; visibility: hidden"
      ></iframe>
    </noscript>
        <h2>搜尋餐廳結果</h2>
   	<div class="wrapper">
   	
    <FORM ACTION="./reserve" method="post">
    <table class="tb1">
    <thead>
        <tr>
            <th>餐廳名稱</th>
            <th>餐廳電話</th>
            <th>餐廳地址</th>
            <th>餐廳類型</th>
            <th>素食種類</th>
            <th>營業時間</th>
            <th>評分</th>
            <th></th>
        </tr>
    </thead>
    <tbody>
    <%--
    	<% List<Restaurant> restaurantList = (List<Restaurant>) request.getAttribute("restaurantList"); %>
        <% for (Restaurant restaurant : restaurantList) {%>
        <tr>
            <td><%= restaurant.getRestaurantName()%></td>
            <td><%= restaurant.getRestaurantTel() %></td>
            <td><%= restaurant.getRestaurantAddress() %></td>
            <td><%= restaurant.getRestaurantCategory() %></td>
            <td><%= restaurant.getRestaurantType() %></td>
            <td><%= restaurant.getRestaurantBusinessHours() %></td>
            <td><%= restaurant.getRestaurantScore() %></td>
            <td><input name="oRestaurant" type="submit" class="btn-success" value="前往訂位"></td>
            <input type="hidden" name="restaurantNumber" value="<%=restaurant.getRestaurantNumber()%>">
           	<input type="hidden" name="restaurantName" value="<%=restaurant.getRestaurantName()%>">
        </tr>
        <% } %>
    --%>
    <c:forEach var="restaurant" items="${ restaurantList }">
    <tr>
		<td> ${restaurant.restaurantName }</td>
        <td> ${restaurant.restaurantTel }</td>
        <td> ${restaurant.restaurantAddress }</td>
        <td> ${restaurant.restaurantCategory }</td>
        <td> ${restaurant.restaurantType }</td>
        <td> ${restaurant.restaurantBusinessHours }</td>
        <td> ${restaurant.restaurantScore }</td>
        <td><div class="btn-warning"><a href="reserve?act&id=${restaurant.restaurantNumber}">前往訂位</a></div></td>
    </tr>
	</c:forEach>
    </tbody>
    </table>
    </FORM>
   <%@include file="/WEB-INF/jsp/parts/map.jspf" %>
   
   </div>
