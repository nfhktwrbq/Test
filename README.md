# Test
Test for Tutu

Приложение содержит два активити. Первое - главное, имеет именю из двух пунктов: Timetable(Расписание) и About. 
По нажатию пункта меню About появляется диалог с информацией. 
При нажатии пунка меню Timetable вызывается новое активити на котором пользователь предоставляется выбор станции отправления,
прибытия, и даты. Станции загружаются из Assets при помощи стандартных библиотек. При выборе станций появляется список с вложениями. 
По вызову контекстного меню на станции - появляется диалог с подробной информацией о станции. 
Поиск необходимой станции осуществляется с помощью поля Search Station... (edittext) и кнопок Next и Prev.
По завершении ввода всей информации появляется кнопка confirm, по нажатии которой информация передаётся в главное активити.

При написании кода программы отладка производилась "вживую"на аппарате Nexus.
Для более эффективной отладки использовался механизм лог сообщений (Log.d("TAG", "message").
Все комментарии в программе - на английском языке. Интерфейс также - английский. Местами (под конец написания кода) имеется стринговый
"хардкод" от которого можно избавится задекларировав соответствующие стринговые переменные в .xml d res директории, а также произвести
локализацию программы.