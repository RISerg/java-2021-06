<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>Клиенты</title>
</head>

<body>
<h4>Добавить клиента</h4>
<form action="clients" method="POST">
    Имя: <input type="text" name="name" value="Иван" placeholder="Имя">
    Телефон: <input type="text" name="phone" value="8(123)456-78-90" placeholder="Телефон">
    Адрес: <input type="text" name="address" value="Адрес адрес адрес" placeholder="Адрес">
    <button type="submit">Сохранить</button>
</form>

<h4>Список клиентов</h4>
<table style="width: 500px">
    <thead>
    <tr>
        <td style="width: 50px">Id</td>
        <td style="width: 150px">Имя</td>
        <td style="width: 100px">Телефон</td>
        <td style="width: 100px">Адрес</td>
    </tr>
    </thead>
    <tbody>
    <#list clients as client>
        <tr>
            <td>${client.id}</td>
            <td>${client.name}</td>
            <td>${client.phone.number}</td>
            <td><#list client.addressList as address>${address.street}<#sep></br></#list></td>
        </tr>
    </#list>
    </tbody>
</table>
</body>
</html>
