# Java Chat App
Client-server chat implemented using Java Socket.

## Building
Для сборки требуется устанновленная Java 17.

`mvn clean package`

### Запуск сервера
Для запуска сервера необходимо указать порт.

`java -jar ./target/java-chat-app-server-${project.version}-jar-with-dependencies <port>`

### Запуск клиента
`javaw -jar ./target/java-chat-app-client-${project.version}-jar-with-dependencies`
