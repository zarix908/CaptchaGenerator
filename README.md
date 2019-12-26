## Сборка проекта:
Из домашней директории(CaptchaService): mvn package 
  
## Запуск приложения:
Из домашней директории(CaptchaService):
1) linux - "java -jar target/CaptchaService-1.0.jar \<CAPTCHA LIFETIME\>"  
2) windows - "java -jar target\CaptchaService-1.0.jar \<CAPTCHA LIFETIME\>"  
CAPTCHA LIFETIME - время жизни капчи в минутах (возможно задание дробным числом, по умолчанию 3 минуты)  
  
## Вызов справки
java -jar CaptchaService-1.0.jar \<-h | --help\>

## Детали реализации 
Сервер поднимается на localhost порт 8080 (изменить адрес и порт можно в application.properties)   
  
GET запрос localhost:8080/generate/ возвращает капчу, 
добавляя необходимые HTTP заголовки. 

GET запрос localhost:8080/shutdown/ завершает работу сервиса, вовзвращает ответ:  
{"response":"service was stopped"}  
  
Для проверки ответа на капчу отправляется POST запрос localhost:8080/checkanswer/
с json в форме {"id":"ID", "answer":"ANSWER"}, сервер 
посылает один из следующих ответов:  
  
1) {"response":"accepted"} - правильный ответ  
2) {"response":"wrong answer"} - неправильный ответ  
3) {"response":"captcha is unavailable"} - капча недействительна  
4) {"response":"wrong id"} - неверный id  
5) {"response":"request should contain json with captcha id and answer"} -   
неверный формат запроса  
6) {"response":id should have following form: HHHHHHHH-HHHH-HHHH-HHHH-HHHHHHHHHHHH, where H - hex digit"} -
неверный формат id  

Приложение представляет собой REST сервис. Для решения 
используются Spring Framework и Spring Boot.
