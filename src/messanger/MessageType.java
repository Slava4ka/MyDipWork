package messanger;

//Инструкции
public enum MessageType {
    USER_NAME, // Имя пользователя
    ENTER, // Вход на сервер
    ADRESS, // Адресат диалога
    MESSAGE_TEXT, // Само сообщение
    GET_KEY_SIDE_A, // Получение ключа
    GET_KEY_SIDE_B,
    STATUS, // Письмо со списком пользователей онлайн
    FATALL_ERROR_THIS_USER_ONLINE,
    ADD_FRIEND,
    FRIENDS_LIST; //Список друзей
}
