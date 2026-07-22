package dev.errnicraft.clientsync.installer;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class I18n {

    public static final class Lang {
        public final String code;
        public final String displayName;

        public Lang(String code, String displayName) {
            this.code = code;
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    public static final Lang[] SUPPORTED = new Lang[] {
            new Lang("ru", "Русский"),
            new Lang("en", "English"),
            new Lang("de", "Deutsch"),
            new Lang("fr", "Français"),
            new Lang("es", "Español"),
            new Lang("pt", "Português"),
            new Lang("uk", "Українська"),
            new Lang("be", "Беларуская"),
            new Lang("kk", "Қазақша"),
            new Lang("pl", "Polski"),
            new Lang("tr", "Türkçe"),
            new Lang("zh", "中文"),
            new Lang("it", "Italiano"),
            new Lang("ja", "日本語"),
            new Lang("ko", "한국어"),
            new Lang("nl", "Nederlands"),
            new Lang("cs", "Čeština"),
            new Lang("sv", "Svenska"),
            new Lang("vi", "Tiếng Việt"),
            new Lang("id", "Bahasa Indonesia"),
            new Lang("ar", "العربية")
    };

    private static final Map<String, Map<String, String>> STRINGS = new LinkedHashMap<>();

    static {
        put("ru", "title", "Подключение к серверу");
        put("ru", "hint", "Укажите адрес и порт сервера, например 127.0.0.1:25566");
        put("ru", "connect", "Подключиться");
        put("ru", "connecting", "Подключение...");
        put("ru", "empty_address", "Введите адрес сервера");
        put("ru", "language", "Язык");
        put("ru", "version_mismatch_title", "Несовместимая версия");
        put("ru", "version_mismatch_message", "Сборка предназначена для Minecraft {server}.\nУ вас установлена версия {client}.\nУстановка невозможна.");
        put("ru", "exit", "Выход");
        put("ru", "clear_history", "Очистить историю адресов");
        put("ru", "loader_mismatch_title", "Несовместимый загрузчик модов");
        put("ru", "loader_mismatch_message", "Сервер требует загрузчик {server_loader} версии {server_loader_version}. \nУ вас установлен {client_loader} ({client_loader_version}). \nУстановка невозможна.");

        put("en", "title", "Connect to server");
        put("en", "hint", "Enter server address and port, e.g. 127.0.0.1:25566");
        put("en", "connect", "Connect");
        put("en", "connecting", "Connecting...");
        put("en", "empty_address", "Enter server address");
        put("en", "language", "Language");
        put("en", "version_mismatch_title", "Incompatible version");
        put("en", "version_mismatch_message", "The build is intended for Minecraft {server}.\nYou have version {client} installed.\nInstallation is not possible.");
        put("en", "exit", "Exit");
        put("en", "clear_history", "Clear address history");
        put("en", "loader_mismatch_title", "Incompatible mod loader");
        put("en", "loader_mismatch_message", "The server requires the {server_loader} loader version {server_loader_version}. \nYou have {client_loader} ({client_loader_version}) installed. \nInstallation is not possible.");

        put("de", "title", "Mit Server verbinden");
        put("de", "hint", "Adresse und Port des Servers eingeben, z. B. 127.0.0.1:25566");
        put("de", "connect", "Verbinden");
        put("de", "connecting", "Verbinde...");
        put("de", "empty_address", "Serveradresse eingeben");
        put("de", "language", "Sprache");
        put("de", "version_mismatch_title", "Inkompatible Version");
        put("de", "version_mismatch_message", "Das Build ist für Minecraft {server} vorgesehen.\nBei Ihnen ist Version {client} installiert.\nInstallation nicht möglich.");
        put("de", "exit", "Beenden");
        put("de", "clear_history", "Adressverlauf löschen");
        put("de", "loader_mismatch_title", "Inkompatibler Mod-Loader");
        put("de", "loader_mismatch_message", "Der Server benötigt den {server_loader}-Loader Version {server_loader_version}. \nBei Ihnen ist {client_loader} ({client_loader_version}) installiert. \nInstallation nicht möglich.");

        put("fr", "title", "Connexion au serveur");
        put("fr", "hint", "Indiquez l'adresse et le port du serveur, ex. 127.0.0.1:25566");
        put("fr", "connect", "Se connecter");
        put("fr", "connecting", "Connexion...");
        put("fr", "empty_address", "Entrez l'adresse du serveur");
        put("fr", "language", "Langue");
        put("fr", "version_mismatch_title", "Version incompatible");
        put("fr", "version_mismatch_message", "Cette build est destinée à Minecraft {server}.\nVous avez la version {client} installée.\nInstallation impossible.");
        put("fr", "exit", "Quitter");
        put("fr", "clear_history", "Effacer l'historique des adresses");
        put("fr", "loader_mismatch_title", "Chargeur de mods incompatible");
        put("fr", "loader_mismatch_message", "Le serveur nécessite le chargeur {server_loader} version {server_loader_version}. \nVous avez {client_loader} ({client_loader_version}) installé. \nInstallation impossible.");

        put("es", "title", "Conexión al servidor");
        put("es", "hint", "Indique la dirección y el puerto del servidor, ej. 127.0.0.1:25566");
        put("es", "connect", "Conectar");
        put("es", "connecting", "Conectando...");
        put("es", "empty_address", "Introduzca la dirección del servidor");
        put("es", "language", "Idioma");
        put("es", "version_mismatch_title", "Versión incompatible");
        put("es", "version_mismatch_message", "La compilación está destinada a Minecraft {server}.\nTiene instalada la versión {client}.\nLa instalación no es posible.");
        put("es", "exit", "Salir");
        put("es", "clear_history", "Borrar historial de direcciones");
        put("es", "loader_mismatch_title", "Cargador de mods incompatible");
        put("es", "loader_mismatch_message", "El servidor requiere el cargador {server_loader} versión {server_loader_version}. \nTiene instalado {client_loader} ({client_loader_version}). \nLa instalación no es posible.");

        put("pt", "title", "Conexão com o servidor");
        put("pt", "hint", "Informe o endereço e a porta do servidor, ex. 127.0.0.1:25566");
        put("pt", "connect", "Conectar");
        put("pt", "connecting", "Conectando...");
        put("pt", "empty_address", "Informe o endereço do servidor");
        put("pt", "language", "Idioma");
        put("pt", "version_mismatch_title", "Versão incompatível");
        put("pt", "version_mismatch_message", "A build é destinada ao Minecraft {server}.\nVocê tem a versão {client} instalada.\nA instalação não é possível.");
        put("pt", "exit", "Sair");
        put("pt", "clear_history", "Limpar histórico de endereços");
        put("pt", "loader_mismatch_title", "Loader de mods incompatível");
        put("pt", "loader_mismatch_message", "O servidor exige o loader {server_loader} versão {server_loader_version}. \nVocê tem {client_loader} ({client_loader_version}) instalado. \nA instalação não é possível.");

        put("uk", "title", "Підключення до сервера");
        put("uk", "hint", "Вкажіть адресу та порт сервера, наприклад 127.0.0.1:25566");
        put("uk", "connect", "Підключитися");
        put("uk", "connecting", "Підключення...");
        put("uk", "empty_address", "Введіть адресу сервера");
        put("uk", "language", "Мова");
        put("uk", "version_mismatch_title", "Несумісна версія");
        put("uk", "version_mismatch_message", "Збірка призначена для Minecraft {server}.\nУ вас встановлена версія {client}.\nВстановлення неможливе.");
        put("uk", "exit", "Вихід");
        put("uk", "clear_history", "Очистити історію адрес");
        put("uk", "loader_mismatch_title", "Несумісний завантажувач модів");
        put("uk", "loader_mismatch_message", "Сервер вимагає завантажувач {server_loader} версії {server_loader_version}. \nУ вас встановлено {client_loader} ({client_loader_version}). \nВстановлення неможливе.");

        put("be", "title", "Падключэнне да сервера");
        put("be", "hint", "Увядзіце адрас і порт сервера, напрыклад 127.0.0.1:25566");
        put("be", "connect", "Падключыцца");
        put("be", "connecting", "Падключэнне...");
        put("be", "empty_address", "Увядзіце адрас сервера");
        put("be", "language", "Мова");
        put("be", "version_mismatch_title", "Несумяшчальная версія");
        put("be", "version_mismatch_message", "Зборка прызначана для Minecraft {server}.\nУ вас усталявана версія {client}.\nУсталяванне немагчымае.");
        put("be", "exit", "Выхад");
        put("be", "clear_history", "Ачысціць гісторыю адрасоў");
        put("be", "loader_mismatch_title", "Несумяшчальны загрузчык модаў");
        put("be", "loader_mismatch_message", "Сервер патрабуе загрузчык {server_loader} версіі {server_loader_version}. \nУ вас усталяваны {client_loader} ({client_loader_version}). \nУсталяванне немагчымае.");

        put("kk", "title", "Серверге қосылу");
        put("kk", "hint", "Сервер мекенжайы мен портын енгізіңіз, мысалы 127.0.0.1:25566");
        put("kk", "connect", "Қосылу");
        put("kk", "connecting", "Қосылуда...");
        put("kk", "empty_address", "Сервер мекенжайын енгізіңіз");
        put("kk", "language", "Тіл");
        put("kk", "version_mismatch_title", "Үйлесімсіз нұсқа");
        put("kk", "version_mismatch_message", "Құрастырылым Minecraft {server} үшін арналған.\nСізде {client} нұсқасы орнатылған.\nОрнату мүмкін емес.");
        put("kk", "exit", "Шығу");
        put("kk", "clear_history", "Мекенжай тарихын тазалау");
        put("kk", "loader_mismatch_title", "Модтар жүктегіші үйлесімсіз");
        put("kk", "loader_mismatch_message", "Сервер {server_loader} жүктегішінің {server_loader_version} нұсқасын талап етеді. \nСізде {client_loader} ({client_loader_version}) орнатылған. \nОрнату мүмкін емес.");

        put("pl", "title", "Połączenie z serwerem");
        put("pl", "hint", "Podaj adres i port serwera, np. 127.0.0.1:25566");
        put("pl", "connect", "Połącz");
        put("pl", "connecting", "Łączenie...");
        put("pl", "empty_address", "Podaj adres serwera");
        put("pl", "language", "Język");
        put("pl", "version_mismatch_title", "Niezgodna wersja");
        put("pl", "version_mismatch_message", "Ta wersja jest przeznaczona dla Minecraft {server}.\nMasz zainstalowaną wersję {client}.\nInstalacja niemożliwa.");
        put("pl", "exit", "Wyjście");
        put("pl", "clear_history", "Wyczyść historię adresów");
        put("pl", "loader_mismatch_title", "Niezgodny loader modów");
        put("pl", "loader_mismatch_message", "Serwer wymaga loadera {server_loader} w wersji {server_loader_version}. \nMasz zainstalowany {client_loader} ({client_loader_version}). \nInstalacja niemożliwa.");

        put("tr", "title", "Sunucuya bağlan");
        put("tr", "hint", "Sunucu adresini ve portunu girin, ör. 127.0.0.1:25566");
        put("tr", "connect", "Bağlan");
        put("tr", "connecting", "Bağlanılıyor...");
        put("tr", "empty_address", "Sunucu adresini girin");
        put("tr", "language", "Dil");
        put("tr", "version_mismatch_title", "Uyumsuz sürüm");
        put("tr", "version_mismatch_message", "Bu yapı Minecraft {server} için tasarlanmıştır.\nSizde {client} sürümü yüklü.\nKurulum mümkün değil.");
        put("tr", "exit", "Çıkış");
        put("tr", "clear_history", "Adres geçmişini temizle");
        put("tr", "loader_mismatch_title", "Uyumsuz mod yükleyicisi");
        put("tr", "loader_mismatch_message", "Sunucu {server_loader} yükleyicisinin {server_loader_version} sürümünü gerektiriyor. \nSizde {client_loader} ({client_loader_version}) yüklü. \nKurulum mümkün değil.");

        put("zh", "title", "连接服务器");
        put("zh", "hint", "请输入服务器地址和端口，例如 127.0.0.1:25566");
        put("zh", "connect", "连接");
        put("zh", "connecting", "正在连接...");
        put("zh", "empty_address", "请输入服务器地址");
        put("zh", "language", "语言");
        put("zh", "version_mismatch_title", "版本不兼容");
        put("zh", "version_mismatch_message", "此构建适用于 Minecraft {server}。\n您安装的是 {client} 版本。\n无法安装。");
        put("zh", "exit", "退出");
        put("zh", "clear_history", "清除地址历史记录");
        put("zh", "loader_mismatch_title", "不兼容的模组加载器");
        put("zh", "loader_mismatch_message", "服务器需要 {server_loader} 加载器版本 {server_loader_version}。 \n您安装的是 {client_loader} ({client_loader_version})。 \n无法安装。");

        put("it", "title", "Connessione al server");
        put("it", "hint", "Inserisci l'indirizzo e la porta del server, es. 127.0.0.1:25566");
        put("it", "connect", "Connetti");
        put("it", "connecting", "Connessione...");
        put("it", "empty_address", "Inserisci l'indirizzo del server");
        put("it", "language", "Lingua");
        put("it", "version_mismatch_title", "Versione incompatibile");
        put("it", "version_mismatch_message", "Questa build è destinata a Minecraft {server}.\nHai installata la versione {client}.\nInstallazione non possibile.");
        put("it", "exit", "Esci");
        put("it", "clear_history", "Cancella cronologia indirizzi");
        put("it", "loader_mismatch_title", "Loader di mod incompatibile");
        put("it", "loader_mismatch_message", "Il server richiede il loader {server_loader} versione {server_loader_version}. \nHai installato {client_loader} ({client_loader_version}). \nInstallazione non possibile.");

        put("ja", "title", "サーバーに接続");
        put("ja", "hint", "サーバーのアドレスとポートを入力してください。例：127.0.0.1:25566");
        put("ja", "connect", "接続");
        put("ja", "connecting", "接続中...");
        put("ja", "empty_address", "サーバーのアドレスを入力してください");
        put("ja", "language", "言語");
        put("ja", "version_mismatch_title", "バージョンが非互換です");
        put("ja", "version_mismatch_message", "このビルドは Minecraft {server} 用です。\nインストールされているバージョンは {client} です。\nインストールできません。");
        put("ja", "exit", "終了");
        put("ja", "clear_history", "アドレス履歴を削除");
        put("ja", "loader_mismatch_title", "互換性のないModローダー");
        put("ja", "loader_mismatch_message", "サーバーは {server_loader} ローダーのバージョン {server_loader_version} を必要としています。 \nインストールされているのは {client_loader} ({client_loader_version}) です。 \nインストールできません。");

        put("ko", "title", "서버에 연결");
        put("ko", "hint", "서버 주소와 포트를 입력하세요. 예: 127.0.0.1:25566");
        put("ko", "connect", "연결");
        put("ko", "connecting", "연결 중...");
        put("ko", "empty_address", "서버 주소를 입력하세요");
        put("ko", "language", "언어");
        put("ko", "version_mismatch_title", "호환되지 않는 버전");
        put("ko", "version_mismatch_message", "이 빌드는 Minecraft {server}용입니다.\n현재 {client} 버전이 설치되어 있습니다.\n설치할 수 없습니다.");
        put("ko", "exit", "종료");
        put("ko", "clear_history", "주소 기록 지우기");
        put("ko", "loader_mismatch_title", "호환되지 않는 모드 로더");
        put("ko", "loader_mismatch_message", "서버는 {server_loader} 로더 버전 {server_loader_version}이(가) 필요합니다. \n현재 {client_loader} ({client_loader_version})가 설치되어 있습니다. \n설치할 수 없습니다.");

        put("nl", "title", "Verbinden met server");
        put("nl", "hint", "Voer het serveradres en de poort in, bijv. 127.0.0.1:25566");
        put("nl", "connect", "Verbinden");
        put("nl", "connecting", "Verbinden...");
        put("nl", "empty_address", "Voer het serveradres in");
        put("nl", "language", "Taal");
        put("nl", "version_mismatch_title", "Incompatibele versie");
        put("nl", "version_mismatch_message", "Deze build is bedoeld voor Minecraft {server}.\nU heeft versie {client} geïnstalleerd.\nInstallatie niet mogelijk.");
        put("nl", "exit", "Afsluiten");
        put("nl", "clear_history", "Adresgeschiedenis wissen");
        put("nl", "loader_mismatch_title", "Incompatibele modloader");
        put("nl", "loader_mismatch_message", "De server vereist de {server_loader}-loader versie {server_loader_version}. \nU heeft {client_loader} ({client_loader_version}) geïnstalleerd. \nInstallatie niet mogelijk.");

        put("cs", "title", "Připojení k serveru");
        put("cs", "hint", "Zadejte adresu a port serveru, např. 127.0.0.1:25566");
        put("cs", "connect", "Připojit");
        put("cs", "connecting", "Připojování...");
        put("cs", "empty_address", "Zadejte adresu serveru");
        put("cs", "language", "Jazyk");
        put("cs", "version_mismatch_title", "Nekompatibilní verze");
        put("cs", "version_mismatch_message", "Tento build je určen pro Minecraft {server}.\nMáte nainstalovanou verzi {client}.\nInstalace není možná.");
        put("cs", "exit", "Konec");
        put("cs", "clear_history", "Vymazat historii adres");
        put("cs", "loader_mismatch_title", "Nekompatibilní loader modů");
        put("cs", "loader_mismatch_message", "Server vyžaduje loader {server_loader} verze {server_loader_version}. \nMáte nainstalovaný {client_loader} ({client_loader_version}). vInstalace není možná.");

        put("sv", "title", "Anslut till server");
        put("sv", "hint", "Ange serverns adress och port, t.ex. 127.0.0.1:25566");
        put("sv", "connect", "Anslut");
        put("sv", "connecting", "Ansluter...");
        put("sv", "empty_address", "Ange serverns adress");
        put("sv", "language", "Språk");
        put("sv", "version_mismatch_title", "Inkompatibel version");
        put("sv", "version_mismatch_message", "Denna build är avsedd för Minecraft {server}.\nDu har version {client} installerad.\nInstallation är inte möjlig.");
        put("sv", "exit", "Avsluta");
        put("sv", "clear_history", "Rensa adresshistorik");
        put("sv", "loader_mismatch_title", "Inkompatibel modladdare");
        put("sv", "loader_mismatch_message", "Servern kräver {server_loader}-laddaren version {server_loader_version}. \nDu har {client_loader} ({client_loader_version}) installerad. \nInstallation är inte möjlig.");

        put("vi", "title", "Kết nối đến máy chủ");
        put("vi", "hint", "Nhập địa chỉ và cổng máy chủ, ví dụ 127.0.0.1:25566");
        put("vi", "connect", "Kết nối");
        put("vi", "connecting", "Đang kết nối...");
        put("vi", "empty_address", "Nhập địa chỉ máy chủ");
        put("vi", "language", "Ngôn ngữ");
        put("vi", "version_mismatch_title", "Phiên bản không tương thích");
        put("vi", "version_mismatch_message", "Bản dựng này dành cho Minecraft {server}.\nBạn đang cài đặt phiên bản {client}.\nKhông thể cài đặt.");
        put("vi", "exit", "Thoát");
        put("vi", "clear_history", "Xóa lịch sử địa chỉ");
        put("vi", "loader_mismatch_title", "Trình tải mod không tương thích");
        put("vi", "loader_mismatch_message", "Máy chủ yêu cầu trình tải {server_loader} phiên bản {server_loader_version}. \nBạn đang cài đặt {client_loader} ({client_loader_version}). \nKhông thể cài đặt.");

        put("id", "title", "Sambungkan ke server");
        put("id", "hint", "Masukkan alamat dan port server, mis. 127.0.0.1:25566");
        put("id", "connect", "Sambungkan");
        put("id", "connecting", "Menyambungkan...");
        put("id", "empty_address", "Masukkan alamat server");
        put("id", "language", "Bahasa");
        put("id", "version_mismatch_title", "Versi tidak kompatibel");
        put("id", "version_mismatch_message", "Build ini ditujukan untuk Minecraft {server}.\nAnda memiliki versi {client} terpasang.\nInstalasi tidak dapat dilakukan.");
        put("id", "exit", "Keluar");
        put("id", "clear_history", "Hapus riwayat alamat");
        put("id", "loader_mismatch_title", "Loader mod tidak kompatibel");
        put("id", "loader_mismatch_message", "Server memerlukan loader {server_loader} versi {server_loader_version}. \nAnda memiliki {client_loader} ({client_loader_version}) terpasang. \nInstalasi tidak dapat dilakukan.");

        put("ar", "title", "الاتصال بالخادم");
        put("ar", "hint", "أدخل عنوان الخادم والمنفذ، مثال 127.0.0.1:25566");
        put("ar", "connect", "اتصال");
        put("ar", "connecting", "جارٍ الاتصال...");
        put("ar", "empty_address", "أدخل عنوان الخادم");
        put("ar", "language", "اللغة");
        put("ar", "version_mismatch_title", "إصدار غير متوافق");
        put("ar", "version_mismatch_message", "هذه النسخة مخصصة لـ Minecraft {server}.\nلديك الإصدار {client} مثبتًا.\nلا يمكن التثبيت.");
        put("ar", "exit", "خروج");
        put("ar", "clear_history", "مسح سجل العناوين");
        put("ar", "loader_mismatch_title", "محمل تعديلات غير متوافق");
        put("ar", "loader_mismatch_message", "يتطلب الخادم محمل {server_loader} بالإصدار {server_loader_version}. \n لدي {client_loader} ({client_loader_version}) مثبتًا. \n لا يمكن التثبيت.");

        // sp_change_new
        put("ru", "sp_change_new", "новый");
        put("en", "sp_change_new", "new");
        put("de", "sp_change_new", "neu");
        put("fr", "sp_change_new", "nouveau");
        put("es", "sp_change_new", "nuevo");
        put("pt", "sp_change_new", "novo");
        put("uk", "sp_change_new", "новий");
        put("be", "sp_change_new", "новы");
        put("kk", "sp_change_new", "жаңа");
        put("pl", "sp_change_new", "nowy");
        put("tr", "sp_change_new", "yeni");
        put("zh", "sp_change_new", "新增");
        put("it", "sp_change_new", "nuovo");
        put("ja", "sp_change_new", "新規");
        put("ko", "sp_change_new", "신규");
        put("nl", "sp_change_new", "nieuw");
        put("cs", "sp_change_new", "nový");
        put("sv", "sp_change_new", "ny");
        put("vi", "sp_change_new", "mới");
        put("id", "sp_change_new", "baru");
        put("ar", "sp_change_new", "جديد");

        // sp_change_update
        put("ru", "sp_change_update", "обновится");
        put("en", "sp_change_update", "update");
        put("de", "sp_change_update", "aktualisieren");
        put("fr", "sp_change_update", "mise à jour");
        put("es", "sp_change_update", "actualizar");
        put("pt", "sp_change_update", "atualizar");
        put("uk", "sp_change_update", "оновиться");
        put("be", "sp_change_update", "абновіцца");
        put("kk", "sp_change_update", "жаңартылады");
        put("pl", "sp_change_update", "aktualizacja");
        put("tr", "sp_change_update", "güncellenecek");
        put("zh", "sp_change_update", "更新");
        put("it", "sp_change_update", "aggiornamento");
        put("ja", "sp_change_update", "更新");
        put("ko", "sp_change_update", "업데이트");
        put("nl", "sp_change_update", "bijwerken");
        put("cs", "sp_change_update", "aktualizace");
        put("sv", "sp_change_update", "uppdatera");
        put("vi", "sp_change_update", "cập nhật");
        put("id", "sp_change_update", "perbarui");
        put("ar", "sp_change_update", "تحديث");

        // sp_change_stale
        put("ru", "sp_change_stale", "удалится");
        put("en", "sp_change_stale", "remove");
        put("de", "sp_change_stale", "entfernen");
        put("fr", "sp_change_stale", "supprimer");
        put("es", "sp_change_stale", "eliminar");
        put("pt", "sp_change_stale", "remover");
        put("uk", "sp_change_stale", "видалиться");
        put("be", "sp_change_stale", "выдаліцца");
        put("kk", "sp_change_stale", "жойылады");
        put("pl", "sp_change_stale", "usunięcie");
        put("tr", "sp_change_stale", "kaldırılacak");
        put("zh", "sp_change_stale", "移除");
        put("it", "sp_change_stale", "rimozione");
        put("ja", "sp_change_stale", "削除");
        put("ko", "sp_change_stale", "삭제");
        put("nl", "sp_change_stale", "verwijderen");
        put("cs", "sp_change_stale", "odstranění");
        put("sv", "sp_change_stale", "ta bort");
        put("vi", "sp_change_stale", "xóa");
        put("id", "sp_change_stale", "hapus");
        put("ar", "sp_change_stale", "إزالة");

        // sp_root_server
        put("ru", "sp_root_server", "Сервер");
        put("en", "sp_root_server", "Server");
        put("de", "sp_root_server", "Server");
        put("fr", "sp_root_server", "Serveur");
        put("es", "sp_root_server", "Servidor");
        put("pt", "sp_root_server", "Servidor");
        put("uk", "sp_root_server", "Сервер");
        put("be", "sp_root_server", "Сервер");
        put("kk", "sp_root_server", "Сервер");
        put("pl", "sp_root_server", "Serwer");
        put("tr", "sp_root_server", "Sunucu");
        put("zh", "sp_root_server", "服务器");
        put("it", "sp_root_server", "Server");
        put("ja", "sp_root_server", "サーバー");
        put("ko", "sp_root_server", "서버");
        put("nl", "sp_root_server", "Server");
        put("cs", "sp_root_server", "Server");
        put("sv", "sp_root_server", "Server");
        put("vi", "sp_root_server", "Máy chủ");
        put("id", "sp_root_server", "Server");
        put("ar", "sp_root_server", "الخادم");

        // sp_install
        put("ru", "sp_install", "Установить");
        put("en", "sp_install", "Install");
        put("de", "sp_install", "Installieren");
        put("fr", "sp_install", "Installer");
        put("es", "sp_install", "Instalar");
        put("pt", "sp_install", "Instalar");
        put("uk", "sp_install", "Встановити");
        put("be", "sp_install", "Усталяваць");
        put("kk", "sp_install", "Орнату");
        put("pl", "sp_install", "Zainstaluj");
        put("tr", "sp_install", "Kur");
        put("zh", "sp_install", "安装");
        put("it", "sp_install", "Installa");
        put("ja", "sp_install", "インストール");
        put("ko", "sp_install", "설치");
        put("nl", "sp_install", "Installeren");
        put("cs", "sp_install", "Nainstalovat");
        put("sv", "sp_install", "Installera");
        put("vi", "sp_install", "Cài đặt");
        put("id", "sp_install", "Instal");
        put("ar", "sp_install", "تثبيت");

        // sp_exclude_forever_suffix
        put("ru", "sp_exclude_forever_suffix", "  [не скачивать]");
        put("en", "sp_exclude_forever_suffix", "  [do not download]");
        put("de", "sp_exclude_forever_suffix", "  [nicht herunterladen]");
        put("fr", "sp_exclude_forever_suffix", "  [ne pas télécharger]");
        put("es", "sp_exclude_forever_suffix", "  [no descargar]");
        put("pt", "sp_exclude_forever_suffix", "  [não baixar]");
        put("uk", "sp_exclude_forever_suffix", "  [не завантажувати]");
        put("be", "sp_exclude_forever_suffix", "  [не спампоўваць]");
        put("kk", "sp_exclude_forever_suffix", "  [жүктемеу]");
        put("pl", "sp_exclude_forever_suffix", "  [nie pobieraj]");
        put("tr", "sp_exclude_forever_suffix", "  [indirme]");
        put("zh", "sp_exclude_forever_suffix", "  [不下载]");
        put("it", "sp_exclude_forever_suffix", "  [non scaricare]");
        put("ja", "sp_exclude_forever_suffix", "  [ダウンロードしない]");
        put("ko", "sp_exclude_forever_suffix", "  [다운로드 안 함]");
        put("nl", "sp_exclude_forever_suffix", "  [niet downloaden]");
        put("cs", "sp_exclude_forever_suffix", "  [nestahovat]");
        put("sv", "sp_exclude_forever_suffix", "  [ladda inte ner]");
        put("vi", "sp_exclude_forever_suffix", "  [không tải]");
        put("id", "sp_exclude_forever_suffix", "  [jangan unduh]");
        put("ar", "sp_exclude_forever_suffix", "  [عدم التنزيل]");

        // sp_clean_install_item
        put("ru", "sp_clean_install_item", "Чистая установка (удалить всё, чего нет на сервере)");
        put("en", "sp_clean_install_item", "Clean install (delete everything not on the server)");
        put("de", "sp_clean_install_item", "Saubere Installation (alles löschen, was nicht auf dem Server ist)");
        put("fr", "sp_clean_install_item", "Installation propre (supprimer tout ce qui n'est pas sur le serveur)");
        put("es", "sp_clean_install_item", "Instalación limpia (eliminar todo lo que no esté en el servidor)");
        put("pt", "sp_clean_install_item", "Instalação limpa (excluir tudo o que não está no servidor)");
        put("uk", "sp_clean_install_item", "Чисте встановлення (видалити все, чого немає на сервері)");
        put("be", "sp_clean_install_item", "Чыстая ўстаноўка (выдаліць усё, чаго няма на серверы)");
        put("kk", "sp_clean_install_item", "Таза орнату (серверде жоқтың барлығын жою)");
        put("pl", "sp_clean_install_item", "Czysta instalacja (usuń wszystko, czego nie ma na serwerze)");
        put("tr", "sp_clean_install_item", "Temiz kurulum (sunucuda olmayan her şeyi sil)");
        put("zh", "sp_clean_install_item", "全新安装（删除服务器上没有的所有内容）");
        put("it", "sp_clean_install_item", "Installazione pulita (elimina tutto ciò che non è sul server)");
        put("ja", "sp_clean_install_item", "クリーンインストール（サーバーにないものをすべて削除）");
        put("ko", "sp_clean_install_item", "클린 설치(서버에 없는 모든 항목 삭제)");
        put("nl", "sp_clean_install_item", "Schone installatie (verwijder alles wat niet op de server staat)");
        put("cs", "sp_clean_install_item", "Čistá instalace (odstranit vše, co není na serveru)");
        put("sv", "sp_clean_install_item", "Ren installation (ta bort allt som inte finns på servern)");
        put("vi", "sp_clean_install_item", "Cài đặt sạch (xóa mọi thứ không có trên máy chủ)");
        put("id", "sp_clean_install_item", "Instalasi bersih (hapus semua yang tidak ada di server)");
        put("ar", "sp_clean_install_item", "تثبيت نظيف (حذف كل ما ليس موجودًا على الخادم)");

        // sp_clean_install_confirm_title
        put("ru", "sp_clean_install_confirm_title", "Чистая установка");
        put("en", "sp_clean_install_confirm_title", "Clean install");
        put("de", "sp_clean_install_confirm_title", "Saubere Installation");
        put("fr", "sp_clean_install_confirm_title", "Installation propre");
        put("es", "sp_clean_install_confirm_title", "Instalación limpia");
        put("pt", "sp_clean_install_confirm_title", "Instalação limpa");
        put("uk", "sp_clean_install_confirm_title", "Чисте встановлення");
        put("be", "sp_clean_install_confirm_title", "Чыстая ўстаноўка");
        put("kk", "sp_clean_install_confirm_title", "Таза орнату");
        put("pl", "sp_clean_install_confirm_title", "Czysta instalacja");
        put("tr", "sp_clean_install_confirm_title", "Temiz kurulum");
        put("zh", "sp_clean_install_confirm_title", "全新安装");
        put("it", "sp_clean_install_confirm_title", "Installazione pulita");
        put("ja", "sp_clean_install_confirm_title", "クリーンインストール");
        put("ko", "sp_clean_install_confirm_title", "클린 설치");
        put("nl", "sp_clean_install_confirm_title", "Schone installatie");
        put("cs", "sp_clean_install_confirm_title", "Čistá instalace");
        put("sv", "sp_clean_install_confirm_title", "Ren installation");
        put("vi", "sp_clean_install_confirm_title", "Cài đặt sạch");
        put("id", "sp_clean_install_confirm_title", "Instalasi bersih");
        put("ar", "sp_clean_install_confirm_title", "تثبيت نظيف");

        // sp_clean_install_confirm_message
        put("ru", "sp_clean_install_confirm_message", "Режим чистой установки: все локальные файлы в папках mods и синхронизируемых директориях,\nкоторых нет на сервере, будут удалены. Файлы вне этих папок (в корне игровой директории) не затрагиваются.\n\nНачать чистую установку сейчас?");
        put("en", "sp_clean_install_confirm_message", "Clean install mode: all local files in the mods folder and synced directories\nthat are not on the server will be deleted. Files outside these folders (in the game directory root) are not affected.\n\nStart clean install now?");
        put("de", "sp_clean_install_confirm_message", "Modus für saubere Installation: Alle lokalen Dateien im mods-Ordner und den synchronisierten Verzeichnissen,\ndie nicht auf dem Server sind, werden gelöscht. Dateien außerhalb dieser Ordner (im Stammverzeichnis des Spiels) sind nicht betroffen.\n\nJetzt saubere Installation starten?");
        put("fr", "sp_clean_install_confirm_message", "Mode d'installation propre : tous les fichiers locaux du dossier mods et des répertoires synchronisés\nqui ne sont pas sur le serveur seront supprimés. Les fichiers hors de ces dossiers (à la racine du répertoire du jeu) ne sont pas concernés.\n\nDémarrer l'installation propre maintenant ?");
        put("es", "sp_clean_install_confirm_message", "Modo de instalación limpia: se eliminarán todos los archivos locales de la carpeta mods y los directorios sincronizados\nque no estén en el servidor. Los archivos fuera de estas carpetas (en la raíz del directorio del juego) no se ven afectados.\n\n¿Iniciar la instalación limpia ahora?");
        put("pt", "sp_clean_install_confirm_message", "Modo de instalação limpa: todos os arquivos locais na pasta mods e diretórios sincronizados\nque não estão no servidor serão excluídos. Arquivos fora dessas pastas (na raiz do diretório do jogo) não são afetados.\n\nIniciar instalação limpa agora?");
        put("uk", "sp_clean_install_confirm_message", "Режим чистого встановлення: усі локальні файли в папці mods та синхронізованих каталогах,\nяких немає на сервері, буде видалено. Файли поза цими папками (у корені ігрового каталогу) не зачіпаються.\n\nПочати чисте встановлення зараз?");
        put("be", "sp_clean_install_confirm_message", "Рэжым чыстай ўстаноўкі: усе лакальныя файлы ў папцы mods і сінхранізаваных каталогах,\nякіх няма на серверы, будуць выдалены. Файлы па-за гэтымі папкамі (у корані гульнявога каталога) не закранаюцца.\n\nПачаць чыстую ўстаноўку зараз?");
        put("kk", "sp_clean_install_confirm_message", "Таза орнату режимі: mods қалтасындағы және синхрондалатын каталогтардағы\nсервердегі жоқ барлық жергілікті файлдар жойылады. Осы қалталардан тыс файлдар (ойын каталогының түбірінде) қозғалмайды.\n\nҚазір таза орнатуды бастау керек пе?");
        put("pl", "sp_clean_install_confirm_message", "Tryb czystej instalacji: wszystkie lokalne pliki w folderze mods i synchronizowanych katalogach,\nktórych nie ma na serwerze, zostaną usunięte. Pliki poza tymi folderami (w katalogu głównym gry) nie są dotknięte.\n\nRozpocząć czystą instalację teraz?");
        put("tr", "sp_clean_install_confirm_message", "Temiz kurulum modu: mods klasöründeki ve senkronize edilen dizinlerdeki\nsunucuda olmayan tüm yerel dosyalar silinecek. Bu klasörlerin dışındaki dosyalar (oyun dizininin kökünde) etkilenmez.\n\nTemiz kurulum şimdi başlatılsın mı?");
        put("zh", "sp_clean_install_confirm_message", "全新安装模式：mods 文件夹和同步目录中所有\n服务器上没有的本地文件都将被删除。这些文件夹之外的文件（游戏目录根目录）不受影响。\n\n现在开始全新安装吗？");
        put("it", "sp_clean_install_confirm_message", "Modalità installazione pulita: tutti i file locali nella cartella mods e nelle directory sincronizzate\nche non sono sul server verranno eliminati. I file al di fuori di queste cartelle (nella radice della directory di gioco) non sono interessati.\n\nAvviare ora l'installazione pulita?");
        put("ja", "sp_clean_install_confirm_message", "クリーンインストールモード：mods フォルダおよび同期対象ディレクトリ内の\nサーバーにないローカルファイルはすべて削除されます。これらのフォルダ外（ゲームディレクトリのルート）のファイルは影響を受けません。\n\n今すぐクリーンインストールを開始しますか？");
        put("ko", "sp_clean_install_confirm_message", "클린 설치 모드: mods 폴더 및 동기화 디렉터리에서\n서버에 없는 모든 로컬 파일이 삭제됩니다. 이 폴더 외부(게임 디렉터리 루트)의 파일은 영향을 받지 않습니다.\n\n지금 클린 설치를 시작하시겠습니까?");
        put("nl", "sp_clean_install_confirm_message", "Modus voor schone installatie: alle lokale bestanden in de map mods en gesynchroniseerde mappen\ndie niet op de server staan, worden verwijderd. Bestanden buiten deze mappen (in de hoofdmap van de spelmap) worden niet beïnvloed.\n\nNu schone installatie starten?");
        put("cs", "sp_clean_install_confirm_message", "Režim čisté instalace: všechny místní soubory ve složce mods a synchronizovaných adresářích,\nkteré nejsou na serveru, budou odstraněny. Soubory mimo tyto složky (v kořeni herního adresáře) nejsou ovlivněny.\n\nZačít čistou instalaci nyní?");
        put("sv", "sp_clean_install_confirm_message", "Läge för ren installation: alla lokala filer i mappen mods och synkroniserade kataloger\nsom inte finns på servern kommer att tas bort. Filer utanför dessa mappar (i spelkatalogens rot) påverkas inte.\n\nStarta ren installation nu?");
        put("vi", "sp_clean_install_confirm_message", "Chế độ cài đặt sạch: tất cả các tệp cục bộ trong thư mục mods và các thư mục đồng bộ\nkhông có trên máy chủ sẽ bị xóa. Các tệp bên ngoài các thư mục này (trong thư mục gốc của trò chơi) không bị ảnh hưởng.\n\nBắt đầu cài đặt sạch ngay bây giờ?");
        put("id", "sp_clean_install_confirm_message", "Mode instalasi bersih: semua file lokal di folder mods dan direktori yang disinkronkan\nyang tidak ada di server akan dihapus. File di luar folder ini (di root direktori game) tidak terpengaruh.\n\nMulai instalasi bersih sekarang?");
        put("ar", "sp_clean_install_confirm_message", "وضع التثبيت النظيف: سيتم حذف جميع الملفات المحلية في مجلد mods والأدلة المتزامنة\nغير الموجودة على الخادم. الملفات خارج هذه المجلدات (في جذر دليل اللعبة) لن تتأثر.\n\nهل تريد بدء التثبيت النظيف الآن؟");

        // sp_required_free_unknown
        put("ru", "sp_required_free_unknown", "Требуется: {required}, свободно: неизвестно");
        put("en", "sp_required_free_unknown", "Required: {required}, free: unknown");
        put("de", "sp_required_free_unknown", "Benötigt: {required}, frei: unbekannt");
        put("fr", "sp_required_free_unknown", "Requis : {required}, libre : inconnu");
        put("es", "sp_required_free_unknown", "Requerido: {required}, libre: desconocido");
        put("pt", "sp_required_free_unknown", "Necessário: {required}, livre: desconhecido");
        put("uk", "sp_required_free_unknown", "Потрібно: {required}, вільно: невідомо");
        put("be", "sp_required_free_unknown", "Патрабуецца: {required}, вольна: невядома");
        put("kk", "sp_required_free_unknown", "Қажет: {required}, бос орын: белгісіз");
        put("pl", "sp_required_free_unknown", "Wymagane: {required}, wolne: nieznane");
        put("tr", "sp_required_free_unknown", "Gerekli: {required}, boş: bilinmiyor");
        put("zh", "sp_required_free_unknown", "需要：{required}，可用空间：未知");
        put("it", "sp_required_free_unknown", "Richiesto: {required}, libero: sconosciuto");
        put("ja", "sp_required_free_unknown", "必要: {required}、空き: 不明");
        put("ko", "sp_required_free_unknown", "필요: {required}, 여유 공간: 알 수 없음");
        put("nl", "sp_required_free_unknown", "Vereist: {required}, vrij: onbekend");
        put("cs", "sp_required_free_unknown", "Vyžadováno: {required}, volné: neznámé");
        put("sv", "sp_required_free_unknown", "Krävs: {required}, ledigt: okänt");
        put("vi", "sp_required_free_unknown", "Yêu cầu: {required}, còn trống: không rõ");
        put("id", "sp_required_free_unknown", "Diperlukan: {required}, tersedia: tidak diketahui");
        put("ar", "sp_required_free_unknown", "مطلوب: {required}، المساحة الحرة: غير معروفة");

        // sp_required_free
        put("ru", "sp_required_free", "Требуется: {required}, свободно: {free}");
        put("en", "sp_required_free", "Required: {required}, free: {free}");
        put("de", "sp_required_free", "Benötigt: {required}, frei: {free}");
        put("fr", "sp_required_free", "Requis : {required}, libre : {free}");
        put("es", "sp_required_free", "Requerido: {required}, libre: {free}");
        put("pt", "sp_required_free", "Necessário: {required}, livre: {free}");
        put("uk", "sp_required_free", "Потрібно: {required}, вільно: {free}");
        put("be", "sp_required_free", "Патрабуецца: {required}, вольна: {free}");
        put("kk", "sp_required_free", "Қажет: {required}, бос орын: {free}");
        put("pl", "sp_required_free", "Wymagane: {required}, wolne: {free}");
        put("tr", "sp_required_free", "Gerekli: {required}, boş: {free}");
        put("zh", "sp_required_free", "需要：{required}，可用空间：{free}");
        put("it", "sp_required_free", "Richiesto: {required}, libero: {free}");
        put("ja", "sp_required_free", "必要: {required}、空き: {free}");
        put("ko", "sp_required_free", "필요: {required}, 여유 공간: {free}");
        put("nl", "sp_required_free", "Vereist: {required}, vrij: {free}");
        put("cs", "sp_required_free", "Vyžadováno: {required}, volné: {free}");
        put("sv", "sp_required_free", "Krävs: {required}, ledigt: {free}");
        put("vi", "sp_required_free", "Yêu cầu: {required}, còn trống: {free}");
        put("id", "sp_required_free", "Diperlukan: {required}, tersedia: {free}");
        put("ar", "sp_required_free", "مطلوب: {required}، المساحة الحرة: {free}");

        // sp_low_disk_space
        put("ru", "sp_low_disk_space", "Недостаточно свободного места на диске");
        put("en", "sp_low_disk_space", "Not enough free disk space");
        put("de", "sp_low_disk_space", "Nicht genügend freier Speicherplatz");
        put("fr", "sp_low_disk_space", "Espace disque libre insuffisant");
        put("es", "sp_low_disk_space", "No hay suficiente espacio libre en disco");
        put("pt", "sp_low_disk_space", "Espaço livre em disco insuficiente");
        put("uk", "sp_low_disk_space", "Недостатньо вільного місця на диску");
        put("be", "sp_low_disk_space", "Недастаткова вольнага месца на дыску");
        put("kk", "sp_low_disk_space", "Дискіде бос орын жеткіліксіз");
        put("pl", "sp_low_disk_space", "Za mało wolnego miejsca na dysku");
        put("tr", "sp_low_disk_space", "Yeterli boş disk alanı yok");
        put("zh", "sp_low_disk_space", "磁盘可用空间不足");
        put("it", "sp_low_disk_space", "Spazio libero su disco insufficiente");
        put("ja", "sp_low_disk_space", "ディスクの空き容量が不足しています");
        put("ko", "sp_low_disk_space", "디스크 여유 공간이 부족합니다");
        put("nl", "sp_low_disk_space", "Onvoldoende vrije schijfruimte");
        put("cs", "sp_low_disk_space", "Nedostatek volného místa na disku");
        put("sv", "sp_low_disk_space", "Inte tillräckligt med ledigt diskutrymme");
        put("vi", "sp_low_disk_space", "Không đủ dung lượng đĩa trống");
        put("id", "sp_low_disk_space", "Ruang disk kosong tidak cukup");
        put("ar", "sp_low_disk_space", "لا توجد مساحة كافية على القرص");

        // sp_all_up_to_date
        put("ru", "sp_all_up_to_date", "Всё актуально, изменений нет.");
        put("en", "sp_all_up_to_date", "Everything is up to date, no changes.");
        put("de", "sp_all_up_to_date", "Alles ist aktuell, keine Änderungen.");
        put("fr", "sp_all_up_to_date", "Tout est à jour, aucun changement.");
        put("es", "sp_all_up_to_date", "Todo está actualizado, sin cambios.");
        put("pt", "sp_all_up_to_date", "Tudo está atualizado, sem alterações.");
        put("uk", "sp_all_up_to_date", "Усе актуально, змін немає.");
        put("be", "sp_all_up_to_date", "Усё актуальна, змен няма.");
        put("kk", "sp_all_up_to_date", "Барлығы өзекті, өзгерістер жоқ.");
        put("pl", "sp_all_up_to_date", "Wszystko aktualne, brak zmian.");
        put("tr", "sp_all_up_to_date", "Her şey güncel, değişiklik yok.");
        put("zh", "sp_all_up_to_date", "一切都是最新的，没有变化。");
        put("it", "sp_all_up_to_date", "Tutto è aggiornato, nessuna modifica.");
        put("ja", "sp_all_up_to_date", "すべて最新です。変更はありません。");
        put("ko", "sp_all_up_to_date", "모두 최신 상태이며 변경 사항이 없습니다.");
        put("nl", "sp_all_up_to_date", "Alles is up-to-date, geen wijzigingen.");
        put("cs", "sp_all_up_to_date", "Vše je aktuální, žádné změny.");
        put("sv", "sp_all_up_to_date", "Allt är uppdaterat, inga ändringar.");
        put("vi", "sp_all_up_to_date", "Mọi thứ đã cập nhật, không có thay đổi.");
        put("id", "sp_all_up_to_date", "Semua sudah terbaru, tidak ada perubahan.");
        put("ar", "sp_all_up_to_date", "كل شيء محدّث، لا توجد تغييرات.");

        // sp_summary
        put("ru", "sp_summary", "Новых: {missing}, обновится: {update}, удалится: {stale}");
        put("en", "sp_summary", "New: {missing}, update: {update}, remove: {stale}");
        put("de", "sp_summary", "Neu: {missing}, Update: {update}, Entfernen: {stale}");
        put("fr", "sp_summary", "Nouveaux : {missing}, mise à jour : {update}, suppression : {stale}");
        put("es", "sp_summary", "Nuevos: {missing}, actualización: {update}, eliminación: {stale}");
        put("pt", "sp_summary", "Novos: {missing}, atualização: {update}, remoção: {stale}");
        put("uk", "sp_summary", "Нових: {missing}, оновиться: {update}, видалиться: {stale}");
        put("be", "sp_summary", "Новых: {missing}, абновіцца: {update}, выдаліцца: {stale}");
        put("kk", "sp_summary", "Жаңа: {missing}, жаңартылады: {update}, жойылады: {stale}");
        put("pl", "sp_summary", "Nowe: {missing}, aktualizacja: {update}, usunięcie: {stale}");
        put("tr", "sp_summary", "Yeni: {missing}, güncellenecek: {update}, kaldırılacak: {stale}");
        put("zh", "sp_summary", "新增：{missing}，更新：{update}，移除：{stale}");
        put("it", "sp_summary", "Nuovi: {missing}, aggiornamento: {update}, rimozione: {stale}");
        put("ja", "sp_summary", "新規: {missing}、更新: {update}、削除: {stale}");
        put("ko", "sp_summary", "신규: {missing}, 업데이트: {update}, 삭제: {stale}");
        put("nl", "sp_summary", "Nieuw: {missing}, bijwerken: {update}, verwijderen: {stale}");
        put("cs", "sp_summary", "Nové: {missing}, aktualizace: {update}, odstranění: {stale}");
        put("sv", "sp_summary", "Nya: {missing}, uppdatera: {update}, ta bort: {stale}");
        put("vi", "sp_summary", "Mới: {missing}, cập nhật: {update}, xóa: {stale}");
        put("id", "sp_summary", "Baru: {missing}, perbarui: {update}, hapus: {stale}");
        put("ar", "sp_summary", "جديد: {missing}، تحديث: {update}، إزالة: {stale}");

        // sp_header_files
        put("ru", "sp_header_files", "  Файлы");
        put("en", "sp_header_files", "  Files");
        put("de", "sp_header_files", "  Dateien");
        put("fr", "sp_header_files", "  Fichiers");
        put("es", "sp_header_files", "  Archivos");
        put("pt", "sp_header_files", "  Arquivos");
        put("uk", "sp_header_files", "  Файли");
        put("be", "sp_header_files", "  Файлы");
        put("kk", "sp_header_files", "  Файлдар");
        put("pl", "sp_header_files", "  Pliki");
        put("tr", "sp_header_files", "  Dosyalar");
        put("zh", "sp_header_files", "  文件");
        put("it", "sp_header_files", "  File");
        put("ja", "sp_header_files", "  ファイル");
        put("ko", "sp_header_files", "  파일");
        put("nl", "sp_header_files", "  Bestanden");
        put("cs", "sp_header_files", "  Soubory");
        put("sv", "sp_header_files", "  Filer");
        put("vi", "sp_header_files", "  Tệp");
        put("id", "sp_header_files", "  File");
        put("ar", "sp_header_files", "  الملفات");

        // sp_header_changes
        put("ru", "sp_header_changes", "  Изменения");
        put("en", "sp_header_changes", "  Changes");
        put("de", "sp_header_changes", "  Änderungen");
        put("fr", "sp_header_changes", "  Modifications");
        put("es", "sp_header_changes", "  Cambios");
        put("pt", "sp_header_changes", "  Alterações");
        put("uk", "sp_header_changes", "  Зміни");
        put("be", "sp_header_changes", "  Змены");
        put("kk", "sp_header_changes", "  Өзгерістер");
        put("pl", "sp_header_changes", "  Zmiany");
        put("tr", "sp_header_changes", "  Değişiklikler");
        put("zh", "sp_header_changes", "  更改");
        put("it", "sp_header_changes", "  Modifiche");
        put("ja", "sp_header_changes", "  変更");
        put("ko", "sp_header_changes", "  변경 사항");
        put("nl", "sp_header_changes", "  Wijzigingen");
        put("cs", "sp_header_changes", "  Změny");
        put("sv", "sp_header_changes", "  Ändringar");
        put("vi", "sp_header_changes", "  Thay đổi");
        put("id", "sp_header_changes", "  Perubahan");
        put("ar", "sp_header_changes", "  التغييرات");

        // pp_preparing
        put("ru", "pp_preparing", "Подготовка...");
        put("en", "pp_preparing", "Preparing...");
        put("de", "pp_preparing", "Vorbereitung...");
        put("fr", "pp_preparing", "Préparation...");
        put("es", "pp_preparing", "Preparando...");
        put("pt", "pp_preparing", "Preparando...");
        put("uk", "pp_preparing", "Підготовка...");
        put("be", "pp_preparing", "Падрыхтоўка...");
        put("kk", "pp_preparing", "Дайындалуда...");
        put("pl", "pp_preparing", "Przygotowywanie...");
        put("tr", "pp_preparing", "Hazırlanıyor...");
        put("zh", "pp_preparing", "准备中...");
        put("it", "pp_preparing", "Preparazione...");
        put("ja", "pp_preparing", "準備中...");
        put("ko", "pp_preparing", "준비 중...");
        put("nl", "pp_preparing", "Voorbereiden...");
        put("cs", "pp_preparing", "Příprava...");
        put("sv", "pp_preparing", "Förbereder...");
        put("vi", "pp_preparing", "Đang chuẩn bị...");
        put("id", "pp_preparing", "Mempersiapkan...");
        put("ar", "pp_preparing", "جارٍ التحضير...");

        // pp_done
        put("ru", "pp_done", "Готово");
        put("en", "pp_done", "Done");
        put("de", "pp_done", "Fertig");
        put("fr", "pp_done", "Terminé");
        put("es", "pp_done", "Listo");
        put("pt", "pp_done", "Concluído");
        put("uk", "pp_done", "Готово");
        put("be", "pp_done", "Гатова");
        put("kk", "pp_done", "Дайын");
        put("pl", "pp_done", "Gotowe");
        put("tr", "pp_done", "Tamamlandı");
        put("zh", "pp_done", "完成");
        put("it", "pp_done", "Fatto");
        put("ja", "pp_done", "完了");
        put("ko", "pp_done", "완료");
        put("nl", "pp_done", "Klaar");
        put("cs", "pp_done", "Hotovo");
        put("sv", "pp_done", "Klart");
        put("vi", "pp_done", "Xong");
        put("id", "pp_done", "Selesai");
        put("ar", "pp_done", "تم");

        // pp_speed_b
        put("ru", "pp_speed_b", "Б/с");
        put("en", "pp_speed_b", "B/s");
        put("de", "pp_speed_b", "B/s");
        put("fr", "pp_speed_b", "o/s");
        put("es", "pp_speed_b", "B/s");
        put("pt", "pp_speed_b", "B/s");
        put("uk", "pp_speed_b", "Б/с");
        put("be", "pp_speed_b", "Б/с");
        put("kk", "pp_speed_b", "Б/с");
        put("pl", "pp_speed_b", "B/s");
        put("tr", "pp_speed_b", "B/s");
        put("zh", "pp_speed_b", "B/秒");
        put("it", "pp_speed_b", "B/s");
        put("ja", "pp_speed_b", "B/秒");
        put("ko", "pp_speed_b", "B/초");
        put("nl", "pp_speed_b", "B/s");
        put("cs", "pp_speed_b", "B/s");
        put("sv", "pp_speed_b", "B/s");
        put("vi", "pp_speed_b", "B/giây");
        put("id", "pp_speed_b", "B/dtk");
        put("ar", "pp_speed_b", "بايت/ث");

        // pp_speed_kb
        put("ru", "pp_speed_kb", "КБ/с");
        put("en", "pp_speed_kb", "KB/s");
        put("de", "pp_speed_kb", "KB/s");
        put("fr", "pp_speed_kb", "Ko/s");
        put("es", "pp_speed_kb", "KB/s");
        put("pt", "pp_speed_kb", "KB/s");
        put("uk", "pp_speed_kb", "КБ/с");
        put("be", "pp_speed_kb", "КБ/с");
        put("kk", "pp_speed_kb", "КБ/с");
        put("pl", "pp_speed_kb", "KB/s");
        put("tr", "pp_speed_kb", "KB/s");
        put("zh", "pp_speed_kb", "KB/秒");
        put("it", "pp_speed_kb", "KB/s");
        put("ja", "pp_speed_kb", "KB/秒");
        put("ko", "pp_speed_kb", "KB/초");
        put("nl", "pp_speed_kb", "KB/s");
        put("cs", "pp_speed_kb", "KB/s");
        put("sv", "pp_speed_kb", "KB/s");
        put("vi", "pp_speed_kb", "KB/giây");
        put("id", "pp_speed_kb", "KB/dtk");
        put("ar", "pp_speed_kb", "ك.ب/ث");

        // pp_speed_mb
        put("ru", "pp_speed_mb", "МБ/с");
        put("en", "pp_speed_mb", "MB/s");
        put("de", "pp_speed_mb", "MB/s");
        put("fr", "pp_speed_mb", "Mo/s");
        put("es", "pp_speed_mb", "MB/s");
        put("pt", "pp_speed_mb", "MB/s");
        put("uk", "pp_speed_mb", "МБ/с");
        put("be", "pp_speed_mb", "МБ/с");
        put("kk", "pp_speed_mb", "МБ/с");
        put("pl", "pp_speed_mb", "MB/s");
        put("tr", "pp_speed_mb", "MB/s");
        put("zh", "pp_speed_mb", "MB/秒");
        put("it", "pp_speed_mb", "MB/s");
        put("ja", "pp_speed_mb", "MB/秒");
        put("ko", "pp_speed_mb", "MB/초");
        put("nl", "pp_speed_mb", "MB/s");
        put("cs", "pp_speed_mb", "MB/s");
        put("sv", "pp_speed_mb", "MB/s");
        put("vi", "pp_speed_mb", "MB/giây");
        put("id", "pp_speed_mb", "MB/dtk");
        put("ar", "pp_speed_mb", "م.ب/ث");

        // pp_speed_gb
        put("ru", "pp_speed_gb", "ГБ/с");
        put("en", "pp_speed_gb", "GB/s");
        put("de", "pp_speed_gb", "GB/s");
        put("fr", "pp_speed_gb", "Go/s");
        put("es", "pp_speed_gb", "GB/s");
        put("pt", "pp_speed_gb", "GB/s");
        put("uk", "pp_speed_gb", "ГБ/с");
        put("be", "pp_speed_gb", "ГБ/с");
        put("kk", "pp_speed_gb", "ГБ/с");
        put("pl", "pp_speed_gb", "GB/s");
        put("tr", "pp_speed_gb", "GB/s");
        put("zh", "pp_speed_gb", "GB/秒");
        put("it", "pp_speed_gb", "GB/s");
        put("ja", "pp_speed_gb", "GB/秒");
        put("ko", "pp_speed_gb", "GB/초");
        put("nl", "pp_speed_gb", "GB/s");
        put("cs", "pp_speed_gb", "GB/s");
        put("sv", "pp_speed_gb", "GB/s");
        put("vi", "pp_speed_gb", "GB/giây");
        put("id", "pp_speed_gb", "GB/dtk");
        put("ar", "pp_speed_gb", "ج.ب/ث");

        // pp_eta_prefix
        put("ru", "pp_eta_prefix", "ETA: ");
        put("en", "pp_eta_prefix", "ETA: ");
        put("de", "pp_eta_prefix", "ETA: ");
        put("fr", "pp_eta_prefix", "ETA : ");
        put("es", "pp_eta_prefix", "ETA: ");
        put("pt", "pp_eta_prefix", "ETA: ");
        put("uk", "pp_eta_prefix", "Залишилось: ");
        put("be", "pp_eta_prefix", "Засталося: ");
        put("kk", "pp_eta_prefix", "Қалды: ");
        put("pl", "pp_eta_prefix", "Pozostało: ");
        put("tr", "pp_eta_prefix", "Kalan süre: ");
        put("zh", "pp_eta_prefix", "预计剩余：");
        put("it", "pp_eta_prefix", "ETA: ");
        put("ja", "pp_eta_prefix", "残り時間: ");
        put("ko", "pp_eta_prefix", "예상 시간: ");
        put("nl", "pp_eta_prefix", "ETA: ");
        put("cs", "pp_eta_prefix", "Zbývá: ");
        put("sv", "pp_eta_prefix", "Återstår: ");
        put("vi", "pp_eta_prefix", "Còn lại: ");
        put("id", "pp_eta_prefix", "Sisa: ");
        put("ar", "pp_eta_prefix", "الوقت المتبقي: ");

        // pp_eta_dash
        put("ru", "pp_eta_dash", "—");
        put("en", "pp_eta_dash", "—");
        put("de", "pp_eta_dash", "—");
        put("fr", "pp_eta_dash", "—");
        put("es", "pp_eta_dash", "—");
        put("pt", "pp_eta_dash", "—");
        put("uk", "pp_eta_dash", "—");
        put("be", "pp_eta_dash", "—");
        put("kk", "pp_eta_dash", "—");
        put("pl", "pp_eta_dash", "—");
        put("tr", "pp_eta_dash", "—");
        put("zh", "pp_eta_dash", "—");
        put("it", "pp_eta_dash", "—");
        put("ja", "pp_eta_dash", "—");
        put("ko", "pp_eta_dash", "—");
        put("nl", "pp_eta_dash", "—");
        put("cs", "pp_eta_dash", "—");
        put("sv", "pp_eta_dash", "—");
        put("vi", "pp_eta_dash", "—");
        put("id", "pp_eta_dash", "—");
        put("ar", "pp_eta_dash", "—");

        // pp_eta_seconds
        put("ru", "pp_eta_seconds", "{s} с");
        put("en", "pp_eta_seconds", "{s} s");
        put("de", "pp_eta_seconds", "{s} s");
        put("fr", "pp_eta_seconds", "{s} s");
        put("es", "pp_eta_seconds", "{s} s");
        put("pt", "pp_eta_seconds", "{s} s");
        put("uk", "pp_eta_seconds", "{s} с");
        put("be", "pp_eta_seconds", "{s} с");
        put("kk", "pp_eta_seconds", "{s} с");
        put("pl", "pp_eta_seconds", "{s} s");
        put("tr", "pp_eta_seconds", "{s} sn");
        put("zh", "pp_eta_seconds", "{s} 秒");
        put("it", "pp_eta_seconds", "{s} s");
        put("ja", "pp_eta_seconds", "{s} 秒");
        put("ko", "pp_eta_seconds", "{s}초");
        put("nl", "pp_eta_seconds", "{s} s");
        put("cs", "pp_eta_seconds", "{s} s");
        put("sv", "pp_eta_seconds", "{s} s");
        put("vi", "pp_eta_seconds", "{s} giây");
        put("id", "pp_eta_seconds", "{s} dtk");
        put("ar", "pp_eta_seconds", "{s} ث");

        // pp_eta_min_sec
        put("ru", "pp_eta_min_sec", "{m} мин {s} с");
        put("en", "pp_eta_min_sec", "{m} min {s} s");
        put("de", "pp_eta_min_sec", "{m} Min {s} s");
        put("fr", "pp_eta_min_sec", "{m} min {s} s");
        put("es", "pp_eta_min_sec", "{m} min {s} s");
        put("pt", "pp_eta_min_sec", "{m} min {s} s");
        put("uk", "pp_eta_min_sec", "{m} хв {s} с");
        put("be", "pp_eta_min_sec", "{m} хв {s} с");
        put("kk", "pp_eta_min_sec", "{m} мин {s} с");
        put("pl", "pp_eta_min_sec", "{m} min {s} s");
        put("tr", "pp_eta_min_sec", "{m} dk {s} sn");
        put("zh", "pp_eta_min_sec", "{m} 分 {s} 秒");
        put("it", "pp_eta_min_sec", "{m} min {s} s");
        put("ja", "pp_eta_min_sec", "{m} 分 {s} 秒");
        put("ko", "pp_eta_min_sec", "{m}분 {s}초");
        put("nl", "pp_eta_min_sec", "{m} min {s} s");
        put("cs", "pp_eta_min_sec", "{m} min {s} s");
        put("sv", "pp_eta_min_sec", "{m} min {s} s");
        put("vi", "pp_eta_min_sec", "{m} phút {s} giây");
        put("id", "pp_eta_min_sec", "{m} mnt {s} dtk");
        put("ar", "pp_eta_min_sec", "{m} د {s} ث");

        // pp_eta_hour_min
        put("ru", "pp_eta_hour_min", "{h} ч {m} мин");
        put("en", "pp_eta_hour_min", "{h} h {m} min");
        put("de", "pp_eta_hour_min", "{h} Std {m} Min");
        put("fr", "pp_eta_hour_min", "{h} h {m} min");
        put("es", "pp_eta_hour_min", "{h} h {m} min");
        put("pt", "pp_eta_hour_min", "{h} h {m} min");
        put("uk", "pp_eta_hour_min", "{h} год {m} хв");
        put("be", "pp_eta_hour_min", "{h} гадз {m} хв");
        put("kk", "pp_eta_hour_min", "{h} сағ {m} мин");
        put("pl", "pp_eta_hour_min", "{h} godz {m} min");
        put("tr", "pp_eta_hour_min", "{h} sa {m} dk");
        put("zh", "pp_eta_hour_min", "{h} 小时 {m} 分");
        put("it", "pp_eta_hour_min", "{h} h {m} min");
        put("ja", "pp_eta_hour_min", "{h} 時間 {m} 分");
        put("ko", "pp_eta_hour_min", "{h}시간 {m}분");
        put("nl", "pp_eta_hour_min", "{h} u {m} min");
        put("cs", "pp_eta_hour_min", "{h} h {m} min");
        put("sv", "pp_eta_hour_min", "{h} tim {m} min");
        put("vi", "pp_eta_hour_min", "{h} giờ {m} phút");
        put("id", "pp_eta_hour_min", "{h} jam {m} mnt");
        put("ar", "pp_eta_hour_min", "{h} س {m} د");

        // im_title
        put("ru", "im_title", "ClientSync — Установщик модпака ({loader})");
        put("en", "im_title", "ClientSync — Modpack Installer ({loader})");
        put("de", "im_title", "ClientSync — Modpack-Installer ({loader})");
        put("fr", "im_title", "ClientSync — Installateur de modpack ({loader})");
        put("es", "im_title", "ClientSync — Instalador de modpack ({loader})");
        put("pt", "im_title", "ClientSync — Instalador de modpack ({loader})");
        put("uk", "im_title", "ClientSync — Встановлювач модпаку ({loader})");
        put("be", "im_title", "ClientSync — Усталёўшчык модпака ({loader})");
        put("kk", "im_title", "ClientSync — Модпак орнатқышы ({loader})");
        put("pl", "im_title", "ClientSync — Instalator modpacka ({loader})");
        put("tr", "im_title", "ClientSync — Modpack Yükleyici ({loader})");
        put("zh", "im_title", "ClientSync — 整合包安装程序（{loader}）");
        put("it", "im_title", "ClientSync — Installatore modpack ({loader})");
        put("ja", "im_title", "ClientSync — モッドパックインストーラー（{loader}）");
        put("ko", "im_title", "ClientSync — 모드팩 설치 프로그램({loader})");
        put("nl", "im_title", "ClientSync — Modpack-installatieprogramma ({loader})");
        put("cs", "im_title", "ClientSync — Instalátor modpacku ({loader})");
        put("sv", "im_title", "ClientSync — Modpack-installerare ({loader})");
        put("vi", "im_title", "ClientSync — Trình cài đặt modpack ({loader})");
        put("id", "im_title", "ClientSync — Installer modpack ({loader})");
        put("ar", "im_title", "ClientSync — مثبّت حزمة التعديلات ({loader})");

        // im_for_suffix
        put("ru", "im_for_suffix", " для ");
        put("en", "im_for_suffix", " for ");
        put("de", "im_for_suffix", " für ");
        put("fr", "im_for_suffix", " pour ");
        put("es", "im_for_suffix", " para ");
        put("pt", "im_for_suffix", " para ");
        put("uk", "im_for_suffix", " для ");
        put("be", "im_for_suffix", " для ");
        put("kk", "im_for_suffix", " үшін ");
        put("pl", "im_for_suffix", " dla ");
        put("tr", "im_for_suffix", " için ");
        put("zh", "im_for_suffix", " 适用于 ");
        put("it", "im_for_suffix", " per ");
        put("ja", "im_for_suffix", " 用 ");
        put("ko", "im_for_suffix", " 대상: ");
        put("nl", "im_for_suffix", " voor ");
        put("cs", "im_for_suffix", " pro ");
        put("sv", "im_for_suffix", " för ");
        put("vi", "im_for_suffix", " cho ");
        put("id", "im_for_suffix", " untuk ");
        put("ar", "im_for_suffix", " لـ ");

        // im_close_installing_title
        put("ru", "im_close_installing_title", "Установка выполняется");
        put("en", "im_close_installing_title", "Installation in progress");
        put("de", "im_close_installing_title", "Installation läuft");
        put("fr", "im_close_installing_title", "Installation en cours");
        put("es", "im_close_installing_title", "Instalación en curso");
        put("pt", "im_close_installing_title", "Instalação em andamento");
        put("uk", "im_close_installing_title", "Встановлення виконується");
        put("be", "im_close_installing_title", "Усталёўка выконваецца");
        put("kk", "im_close_installing_title", "Орнату жүруде");
        put("pl", "im_close_installing_title", "Instalacja w toku");
        put("tr", "im_close_installing_title", "Kurulum devam ediyor");
        put("zh", "im_close_installing_title", "正在安装");
        put("it", "im_close_installing_title", "Installazione in corso");
        put("ja", "im_close_installing_title", "インストール中");
        put("ko", "im_close_installing_title", "설치 진행 중");
        put("nl", "im_close_installing_title", "Installatie bezig");
        put("cs", "im_close_installing_title", "Probíhá instalace");
        put("sv", "im_close_installing_title", "Installation pågår");
        put("vi", "im_close_installing_title", "Đang cài đặt");
        put("id", "im_close_installing_title", "Instalasi sedang berlangsung");
        put("ar", "im_close_installing_title", "التثبيت قيد التنفيذ");

        // im_close_installing_message
        put("ru", "im_close_installing_message", "Файлы уже устанавливаются в игровую директорию.\nПрерывание может оставить модпак в неполном состоянии.\nВсё равно закрыть установщик?");
        put("en", "im_close_installing_message", "Files are already being installed into the game directory.\nInterrupting may leave the modpack in an incomplete state.\nClose the installer anyway?");
        put("de", "im_close_installing_message", "Dateien werden bereits in das Spielverzeichnis installiert.\nEin Abbruch kann das Modpack in einem unvollständigen Zustand hinterlassen.\nInstallationsprogramm trotzdem schließen?");
        put("fr", "im_close_installing_message", "Les fichiers sont déjà en cours d'installation dans le répertoire du jeu.\nUne interruption peut laisser le modpack dans un état incomplet.\nFermer quand même l'installateur ?");
        put("es", "im_close_installing_message", "Los archivos ya se están instalando en el directorio del juego.\nInterrumpir puede dejar el modpack en un estado incompleto.\n¿Cerrar el instalador de todos modos?");
        put("pt", "im_close_installing_message", "Os arquivos já estão sendo instalados no diretório do jogo.\nInterromper pode deixar o modpack em um estado incompleto.\nFechar o instalador mesmo assim?");
        put("uk", "im_close_installing_message", "Файли вже встановлюються в ігровий каталог.\nПереривання може залишити модпак у неповному стані.\nВсе одно закрити встановлювач?");
        put("be", "im_close_installing_message", "Файлы ўжо ўсталёўваюцца ў гульнявы каталог.\nПерапыненне можа пакінуць модпак у няпоўным стане.\nУсё роўна закрыць усталёўшчык?");
        put("kk", "im_close_installing_message", "Файлдар ойын каталогына орнатылып жатыр.\nҮзу модпакты толық емес күйде қалдыруы мүмкін.\nБарлық жағдайда орнатқышты жабу керек пе?");
        put("pl", "im_close_installing_message", "Pliki są już instalowane w katalogu gry.\nPrzerwanie może pozostawić modpack w niekompletnym stanie.\nZamknąć instalator mimo to?");
        put("tr", "im_close_installing_message", "Dosyalar zaten oyun dizinine kuruluyor.\nKesinti, mod paketini eksik durumda bırakabilir.\nYükleyici yine de kapatılsın mı?");
        put("zh", "im_close_installing_message", "文件正在安装到游戏目录中。\n中断可能导致整合包处于不完整状态。\n仍要关闭安装程序吗？");
        put("it", "im_close_installing_message", "I file sono già in fase di installazione nella directory di gioco.\nL'interruzione potrebbe lasciare il modpack in uno stato incompleto.\nChiudere comunque l'installatore?");
        put("ja", "im_close_installing_message", "ファイルはすでにゲームディレクトリにインストールされています。\n中断するとモッドパックが不完全な状態のままになる可能性があります。\nそれでもインストーラーを閉じますか？");
        put("ko", "im_close_installing_message", "파일이 이미 게임 디렉터리에 설치되고 있습니다.\n중단하면 모드팩이 불완전한 상태로 남을 수 있습니다.\n그래도 설치 프로그램을 닫으시겠습니까?");
        put("nl", "im_close_installing_message", "Bestanden worden al geïnstalleerd in de spelmap.\nOnderbreken kan de modpack in een onvolledige staat achterlaten.\nInstallatieprogramma toch sluiten?");
        put("cs", "im_close_installing_message", "Soubory se již instalují do herního adresáře.\nPřerušení může modpack zanechat v neúplném stavu.\nPřesto zavřít instalátor?");
        put("sv", "im_close_installing_message", "Filer installeras redan i spelkatalogen.\nAtt avbryta kan lämna modpacket i ett ofullständigt tillstånd.\nStäng installationsprogrammet ändå?");
        put("vi", "im_close_installing_message", "Các tệp đang được cài đặt vào thư mục trò chơi.\nViệc gián đoạn có thể khiến modpack ở trạng thái không đầy đủ.\nVẫn đóng trình cài đặt?");
        put("id", "im_close_installing_message", "File sedang diinstal ke direktori game.\nMenginterupsi dapat membuat modpack dalam keadaan tidak lengkap.\nTetap tutup installer?");
        put("ar", "im_close_installing_message", "جارٍ تثبيت الملفات في دليل اللعبة بالفعل.\nقد يؤدي المقاطعة إلى ترك حزمة التعديلات في حالة غير مكتملة.\nهل تريد إغلاق المثبّت على أي حال؟");

        // im_close_downloading_title
        put("ru", "im_close_downloading_title", "Закрыть установщик?");
        put("en", "im_close_downloading_title", "Close the installer?");
        put("de", "im_close_downloading_title", "Installationsprogramm schließen?");
        put("fr", "im_close_downloading_title", "Fermer l'installateur ?");
        put("es", "im_close_downloading_title", "¿Cerrar el instalador?");
        put("pt", "im_close_downloading_title", "Fechar o instalador?");
        put("uk", "im_close_downloading_title", "Закрити встановлювач?");
        put("be", "im_close_downloading_title", "Закрыць усталёўшчык?");
        put("kk", "im_close_downloading_title", "Орнатқышты жабу керек пе?");
        put("pl", "im_close_downloading_title", "Zamknąć instalator?");
        put("tr", "im_close_downloading_title", "Yükleyici kapatılsın mı?");
        put("zh", "im_close_downloading_title", "关闭安装程序？");
        put("it", "im_close_downloading_title", "Chiudere l'installatore?");
        put("ja", "im_close_downloading_title", "インストーラーを閉じますか？");
        put("ko", "im_close_downloading_title", "설치 프로그램을 닫으시겠습니까?");
        put("nl", "im_close_downloading_title", "Installatieprogramma sluiten?");
        put("cs", "im_close_downloading_title", "Zavřít instalátor?");
        put("sv", "im_close_downloading_title", "Stäng installationsprogrammet?");
        put("vi", "im_close_downloading_title", "Đóng trình cài đặt?");
        put("id", "im_close_downloading_title", "Tutup installer?");
        put("ar", "im_close_downloading_title", "هل تريد إغلاق المثبّت؟");

        // im_close_downloading_message
        put("ru", "im_close_downloading_message", "Скачивание ещё не завершено.\nЕсли закрыть сейчас, скачанные файлы будут удалены, изменения применены не будут.\nЗакрыть установщик?");
        put("en", "im_close_downloading_message", "Download is not yet complete.\nIf you close now, downloaded files will be deleted and changes will not be applied.\nClose the installer?");
        put("de", "im_close_downloading_message", "Der Download ist noch nicht abgeschlossen.\nWenn Sie jetzt schließen, werden heruntergeladene Dateien gelöscht und Änderungen nicht angewendet.\nInstallationsprogramm schließen?");
        put("fr", "im_close_downloading_message", "Le téléchargement n'est pas encore terminé.\nSi vous fermez maintenant, les fichiers téléchargés seront supprimés et les modifications ne seront pas appliquées.\nFermer l'installateur ?");
        put("es", "im_close_downloading_message", "La descarga aún no ha finalizado.\nSi cierra ahora, los archivos descargados se eliminarán y los cambios no se aplicarán.\n¿Cerrar el instalador?");
        put("pt", "im_close_downloading_message", "O download ainda não foi concluído.\nSe você fechar agora, os arquivos baixados serão excluídos e as alterações não serão aplicadas.\nFechar o instalador?");
        put("uk", "im_close_downloading_message", "Завантаження ще не завершено.\nЯкщо закрити зараз, завантажені файли буде видалено, зміни застосовані не будуть.\nЗакрити встановлювач?");
        put("be", "im_close_downloading_message", "Спампоўка яшчэ не завершана.\nКалі закрыць зараз, спампаваныя файлы будуць выдалены, змены ужыты не будуць.\nЗакрыць усталёўшчык?");
        put("kk", "im_close_downloading_message", "Жүктеп алу әлі аяқталған жоқ.\nҚазір жапсаңыз, жүктелген файлдар жойылады, өзгерістер қолданылмайды.\nОрнатқышты жабу керек пе?");
        put("pl", "im_close_downloading_message", "Pobieranie nie zostało jeszcze zakończone.\nJeśli zamkniesz teraz, pobrane pliki zostaną usunięte, zmiany nie zostaną zastosowane.\nZamknąć instalator?");
        put("tr", "im_close_downloading_message", "İndirme henüz tamamlanmadı.\nŞimdi kapatırsanız, indirilen dosyalar silinecek ve değişiklikler uygulanmayacaktır.\nYükleyici kapatılsın mı?");
        put("zh", "im_close_downloading_message", "下载尚未完成。\n如果现在关闭，已下载的文件将被删除，更改不会应用。\n关闭安装程序？");
        put("it", "im_close_downloading_message", "Il download non è ancora completo.\nSe chiudi ora, i file scaricati verranno eliminati e le modifiche non verranno applicate.\nChiudere l'installatore?");
        put("ja", "im_close_downloading_message", "ダウンロードはまだ完了していません。\n今閉じると、ダウンロード済みのファイルは削除され、変更は適用されません。\nインストーラーを閉じますか？");
        put("ko", "im_close_downloading_message", "다운로드가 아직 완료되지 않았습니다.\n지금 닫으면 다운로드한 파일이 삭제되고 변경 사항이 적용되지 않습니다.\n설치 프로그램을 닫으시겠습니까?");
        put("nl", "im_close_downloading_message", "Download is nog niet voltooid.\nAls u nu sluit, worden gedownloade bestanden verwijderd en wijzigingen niet toegepast.\nInstallatieprogramma sluiten?");
        put("cs", "im_close_downloading_message", "Stahování ještě není dokončeno.\nPokud nyní zavřete, stažené soubory budou odstraněny, změny nebudou použity.\nZavřít instalátor?");
        put("sv", "im_close_downloading_message", "Nedladdningen är inte klar än.\nOm du stänger nu kommer nedladdade filer att tas bort och ändringar tillämpas inte.\nStäng installationsprogrammet?");
        put("vi", "im_close_downloading_message", "Quá trình tải xuống chưa hoàn tất.\nNếu đóng ngay bây giờ, các tệp đã tải sẽ bị xóa và các thay đổi sẽ không được áp dụng.\nĐóng trình cài đặt?");
        put("id", "im_close_downloading_message", "Unduhan belum selesai.\nJika Anda menutup sekarang, file yang diunduh akan dihapus dan perubahan tidak akan diterapkan.\nTutup installer?");
        put("ar", "im_close_downloading_message", "لم يكتمل التنزيل بعد.\nإذا أغلقت الآن، فسيتم حذف الملفات التي تم تنزيلها ولن يتم تطبيق التغييرات.\nهل تريد إغلاق المثبّت؟");

        // im_close_confirm_title
        put("ru", "im_close_confirm_title", "Закрыть установщик?");
        put("en", "im_close_confirm_title", "Close the installer?");
        put("de", "im_close_confirm_title", "Installationsprogramm schließen?");
        put("fr", "im_close_confirm_title", "Fermer l'installateur ?");
        put("es", "im_close_confirm_title", "¿Cerrar el instalador?");
        put("pt", "im_close_confirm_title", "Fechar o instalador?");
        put("uk", "im_close_confirm_title", "Закрити встановлювач?");
        put("be", "im_close_confirm_title", "Закрыць усталёўшчык?");
        put("kk", "im_close_confirm_title", "Орнатқышты жабу керек пе?");
        put("pl", "im_close_confirm_title", "Zamknąć instalator?");
        put("tr", "im_close_confirm_title", "Yükleyici kapatılsın mı?");
        put("zh", "im_close_confirm_title", "关闭安装程序？");
        put("it", "im_close_confirm_title", "Chiudere l'installatore?");
        put("ja", "im_close_confirm_title", "インストーラーを閉じますか？");
        put("ko", "im_close_confirm_title", "설치 프로그램을 닫으시겠습니까?");
        put("nl", "im_close_confirm_title", "Installatieprogramma sluiten?");
        put("cs", "im_close_confirm_title", "Zavřít instalátor?");
        put("sv", "im_close_confirm_title", "Stäng installationsprogrammet?");
        put("vi", "im_close_confirm_title", "Đóng trình cài đặt?");
        put("id", "im_close_confirm_title", "Tutup installer?");
        put("ar", "im_close_confirm_title", "هل تريد إغلاق المثبّت؟");

        // im_close_confirm_message
        put("ru", "im_close_confirm_message", "Вы уверены, что хотите выйти из установщика?");
        put("en", "im_close_confirm_message", "Are you sure you want to exit the installer?");
        put("de", "im_close_confirm_message", "Sind Sie sicher, dass Sie das Installationsprogramm beenden möchten?");
        put("fr", "im_close_confirm_message", "Voulez-vous vraiment quitter l'installateur ?");
        put("es", "im_close_confirm_message", "¿Está seguro de que desea salir del instalador?");
        put("pt", "im_close_confirm_message", "Tem certeza de que deseja sair do instalador?");
        put("uk", "im_close_confirm_message", "Ви впевнені, що хочете вийти з встановлювача?");
        put("be", "im_close_confirm_message", "Вы ўпэўнены, што хочаце выйсці з усталёўшчыка?");
        put("kk", "im_close_confirm_message", "Орнатқыштан шығуға сенімдісіз бе?");
        put("pl", "im_close_confirm_message", "Czy na pewno chcesz wyjść z instalatora?");
        put("tr", "im_close_confirm_message", "Yükleyiciden çıkmak istediğinizden emin misiniz?");
        put("zh", "im_close_confirm_message", "确定要退出安装程序吗？");
        put("it", "im_close_confirm_message", "Sei sicuro di voler uscire dall'installatore?");
        put("ja", "im_close_confirm_message", "インストーラーを終了してもよろしいですか？");
        put("ko", "im_close_confirm_message", "설치 프로그램을 종료하시겠습니까?");
        put("nl", "im_close_confirm_message", "Weet u zeker dat u het installatieprogramma wilt afsluiten?");
        put("cs", "im_close_confirm_message", "Opravdu chcete ukončit instalátor?");
        put("sv", "im_close_confirm_message", "Är du säker på att du vill avsluta installationsprogrammet?");
        put("vi", "im_close_confirm_message", "Bạn có chắc chắn muốn thoát khỏi trình cài đặt không?");
        put("id", "im_close_confirm_message", "Apakah Anda yakin ingin keluar dari installer?");
        put("ar", "im_close_confirm_message", "هل أنت متأكد من أنك تريد الخروج من المثبّت؟");

        // im_ping_failed
        put("ru", "im_ping_failed", "Сервер не отвечает или это не ClientSync-сервер");
        put("en", "im_ping_failed", "Server is not responding or this is not a ClientSync server");
        put("de", "im_ping_failed", "Server antwortet nicht oder dies ist kein ClientSync-Server");
        put("fr", "im_ping_failed", "Le serveur ne répond pas ou ce n'est pas un serveur ClientSync");
        put("es", "im_ping_failed", "El servidor no responde o no es un servidor ClientSync");
        put("pt", "im_ping_failed", "O servidor não responde ou não é um servidor ClientSync");
        put("uk", "im_ping_failed", "Сервер не відповідає або це не ClientSync-сервер");
        put("be", "im_ping_failed", "Сервер не адказвае або гэта не ClientSync-сервер");
        put("kk", "im_ping_failed", "Сервер жауап бермейді немесе бұл ClientSync сервері емес");
        put("pl", "im_ping_failed", "Serwer nie odpowiada lub to nie jest serwer ClientSync");
        put("tr", "im_ping_failed", "Sunucu yanıt vermiyor veya bu bir ClientSync sunucusu değil");
        put("zh", "im_ping_failed", "服务器无响应，或者这不是 ClientSync 服务器");
        put("it", "im_ping_failed", "Il server non risponde oppure non è un server ClientSync");
        put("ja", "im_ping_failed", "サーバーが応答しないか、ClientSync サーバーではありません");
        put("ko", "im_ping_failed", "서버가 응답하지 않거나 ClientSync 서버가 아닙니다");
        put("nl", "im_ping_failed", "Server reageert niet of dit is geen ClientSync-server");
        put("cs", "im_ping_failed", "Server neodpovídá nebo se nejedná o ClientSync server");
        put("sv", "im_ping_failed", "Servern svarar inte eller så är detta inte en ClientSync-server");
        put("vi", "im_ping_failed", "Máy chủ không phản hồi hoặc đây không phải là máy chủ ClientSync");
        put("id", "im_ping_failed", "Server tidak merespons atau ini bukan server ClientSync");
        put("ar", "im_ping_failed", "الخادم لا يستجيب أو أن هذا ليس خادم ClientSync");

        // im_connect_failed
        put("ru", "im_connect_failed", "Не удалось подключиться: {error}");
        put("en", "im_connect_failed", "Failed to connect: {error}");
        put("de", "im_connect_failed", "Verbindung fehlgeschlagen: {error}");
        put("fr", "im_connect_failed", "Échec de la connexion : {error}");
        put("es", "im_connect_failed", "Error al conectar: {error}");
        put("pt", "im_connect_failed", "Falha ao conectar: {error}");
        put("uk", "im_connect_failed", "Не вдалося підключитися: {error}");
        put("be", "im_connect_failed", "Не ўдалося падключыцца: {error}");
        put("kk", "im_connect_failed", "Қосылу мүмкін болмады: {error}");
        put("pl", "im_connect_failed", "Nie udało się połączyć: {error}");
        put("tr", "im_connect_failed", "Bağlantı kurulamadı: {error}");
        put("zh", "im_connect_failed", "连接失败：{error}");
        put("it", "im_connect_failed", "Impossibile connettersi: {error}");
        put("ja", "im_connect_failed", "接続に失敗しました: {error}");
        put("ko", "im_connect_failed", "연결하지 못했습니다: {error}");
        put("nl", "im_connect_failed", "Verbinden mislukt: {error}");
        put("cs", "im_connect_failed", "Nepodařilo se připojit: {error}");
        put("sv", "im_connect_failed", "Det gick inte att ansluta: {error}");
        put("vi", "im_connect_failed", "Kết nối không thành công: {error}");
        put("id", "im_connect_failed", "Gagal terhubung: {error}");
        put("ar", "im_connect_failed", "فشل الاتصال: {error}");

        // im_nothing_to_install_title
        put("ru", "im_nothing_to_install_title", "Нечего устанавливать");
        put("en", "im_nothing_to_install_title", "Nothing to install");
        put("de", "im_nothing_to_install_title", "Nichts zu installieren");
        put("fr", "im_nothing_to_install_title", "Rien à installer");
        put("es", "im_nothing_to_install_title", "Nada que instalar");
        put("pt", "im_nothing_to_install_title", "Nada para instalar");
        put("uk", "im_nothing_to_install_title", "Нічого встановлювати");
        put("be", "im_nothing_to_install_title", "Няма чаго ўсталёўваць");
        put("kk", "im_nothing_to_install_title", "Орнатуға ештеңе жоқ");
        put("pl", "im_nothing_to_install_title", "Nie ma nic do zainstalowania");
        put("tr", "im_nothing_to_install_title", "Kurulacak bir şey yok");
        put("zh", "im_nothing_to_install_title", "没有可安装的内容");
        put("it", "im_nothing_to_install_title", "Niente da installare");
        put("ja", "im_nothing_to_install_title", "インストールするものがありません");
        put("ko", "im_nothing_to_install_title", "설치할 항목이 없습니다");
        put("nl", "im_nothing_to_install_title", "Niets om te installeren");
        put("cs", "im_nothing_to_install_title", "Není co instalovat");
        put("sv", "im_nothing_to_install_title", "Inget att installera");
        put("vi", "im_nothing_to_install_title", "Không có gì để cài đặt");
        put("id", "im_nothing_to_install_title", "Tidak ada yang perlu diinstal");
        put("ar", "im_nothing_to_install_title", "لا يوجد شيء للتثبيت");

        // im_nothing_to_install_message
        put("ru", "im_nothing_to_install_message", "Все файлы сняты с установки. Отметьте хотя бы один файл или папку.");
        put("en", "im_nothing_to_install_message", "All files are excluded from installation. Select at least one file or folder.");
        put("de", "im_nothing_to_install_message", "Alle Dateien sind von der Installation ausgeschlossen. Wählen Sie mindestens eine Datei oder einen Ordner aus.");
        put("fr", "im_nothing_to_install_message", "Tous les fichiers sont exclus de l'installation. Sélectionnez au moins un fichier ou un dossier.");
        put("es", "im_nothing_to_install_message", "Todos los archivos están excluidos de la instalación. Seleccione al menos un archivo o carpeta.");
        put("pt", "im_nothing_to_install_message", "Todos os arquivos estão excluídos da instalação. Selecione pelo menos um arquivo ou pasta.");
        put("uk", "im_nothing_to_install_message", "Усі файли знято з встановлення. Позначте хоча б один файл або папку.");
        put("be", "im_nothing_to_install_message", "Усе файлы знятыя з усталёўкі. Пазначце хаця б адзін файл ці папку.");
        put("kk", "im_nothing_to_install_message", "Барлық файлдар орнатудан алынып тасталды. Кемінде бір файлды немесе қалтаны белгілеңіз.");
        put("pl", "im_nothing_to_install_message", "Wszystkie pliki zostały wyłączone z instalacji. Zaznacz co najmniej jeden plik lub folder.");
        put("tr", "im_nothing_to_install_message", "Tüm dosyalar kurulumdan çıkarıldı. En az bir dosya veya klasör seçin.");
        put("zh", "im_nothing_to_install_message", "所有文件都已从安装中排除。请至少选择一个文件或文件夹。");
        put("it", "im_nothing_to_install_message", "Tutti i file sono stati esclusi dall'installazione. Seleziona almeno un file o una cartella.");
        put("ja", "im_nothing_to_install_message", "すべてのファイルがインストール対象から除外されています。少なくとも1つのファイルまたはフォルダを選択してください。");
        put("ko", "im_nothing_to_install_message", "모든 파일이 설치에서 제외되었습니다. 파일 또는 폴더를 하나 이상 선택하세요.");
        put("nl", "im_nothing_to_install_message", "Alle bestanden zijn uitgesloten van installatie. Selecteer ten minste één bestand of map.");
        put("cs", "im_nothing_to_install_message", "Všechny soubory jsou vyloučeny z instalace. Vyberte alespoň jeden soubor nebo složku.");
        put("sv", "im_nothing_to_install_message", "Alla filer är uteslutna från installationen. Välj minst en fil eller mapp.");
        put("vi", "im_nothing_to_install_message", "Tất cả các tệp đã bị loại khỏi cài đặt. Hãy chọn ít nhất một tệp hoặc thư mục.");
        put("id", "im_nothing_to_install_message", "Semua file dikecualikan dari instalasi. Pilih setidaknya satu file atau folder.");
        put("ar", "im_nothing_to_install_message", "تم استبعاد جميع الملفات من التثبيت. حدد ملفًا واحدًا على الأقل أو مجلدًا.");

        // im_status_downloading
        put("ru", "im_status_downloading", "Скачивание файлов...");
        put("en", "im_status_downloading", "Downloading files...");
        put("de", "im_status_downloading", "Dateien werden heruntergeladen...");
        put("fr", "im_status_downloading", "Téléchargement des fichiers...");
        put("es", "im_status_downloading", "Descargando archivos...");
        put("pt", "im_status_downloading", "Baixando arquivos...");
        put("uk", "im_status_downloading", "Завантаження файлів...");
        put("be", "im_status_downloading", "Спампоўка файлаў...");
        put("kk", "im_status_downloading", "Файлдар жүктелуде...");
        put("pl", "im_status_downloading", "Pobieranie plików...");
        put("tr", "im_status_downloading", "Dosyalar indiriliyor...");
        put("zh", "im_status_downloading", "正在下载文件...");
        put("it", "im_status_downloading", "Download dei file...");
        put("ja", "im_status_downloading", "ファイルをダウンロード中...");
        put("ko", "im_status_downloading", "파일 다운로드 중...");
        put("nl", "im_status_downloading", "Bestanden downloaden...");
        put("cs", "im_status_downloading", "Stahování souborů...");
        put("sv", "im_status_downloading", "Laddar ner filer...");
        put("vi", "im_status_downloading", "Đang tải tệp...");
        put("id", "im_status_downloading", "Mengunduh file...");
        put("ar", "im_status_downloading", "جارٍ تنزيل الملفات...");

        // im_status_applying
        put("ru", "im_status_applying", "Применение изменений...");
        put("en", "im_status_applying", "Applying changes...");
        put("de", "im_status_applying", "Änderungen werden angewendet...");
        put("fr", "im_status_applying", "Application des modifications...");
        put("es", "im_status_applying", "Aplicando cambios...");
        put("pt", "im_status_applying", "Aplicando alterações...");
        put("uk", "im_status_applying", "Застосування змін...");
        put("be", "im_status_applying", "Ужыванне змен...");
        put("kk", "im_status_applying", "Өзгерістер қолданылуда...");
        put("pl", "im_status_applying", "Stosowanie zmian...");
        put("tr", "im_status_applying", "Değişiklikler uygulanıyor...");
        put("zh", "im_status_applying", "正在应用更改...");
        put("it", "im_status_applying", "Applicazione delle modifiche...");
        put("ja", "im_status_applying", "変更を適用中...");
        put("ko", "im_status_applying", "변경 사항 적용 중...");
        put("nl", "im_status_applying", "Wijzigingen toepassen...");
        put("cs", "im_status_applying", "Používání změn...");
        put("sv", "im_status_applying", "Tillämpar ändringar...");
        put("vi", "im_status_applying", "Đang áp dụng thay đổi...");
        put("id", "im_status_applying", "Menerapkan perubahan...");
        put("ar", "im_status_applying", "جارٍ تطبيق التغييرات...");

        // im_log_downloaded
        put("ru", "im_log_downloaded", "Скачано: {key}");
        put("en", "im_log_downloaded", "Downloaded: {key}");
        put("de", "im_log_downloaded", "Heruntergeladen: {key}");
        put("fr", "im_log_downloaded", "Téléchargé : {key}");
        put("es", "im_log_downloaded", "Descargado: {key}");
        put("pt", "im_log_downloaded", "Baixado: {key}");
        put("uk", "im_log_downloaded", "Завантажено: {key}");
        put("be", "im_log_downloaded", "Спампавана: {key}");
        put("kk", "im_log_downloaded", "Жүктелді: {key}");
        put("pl", "im_log_downloaded", "Pobrano: {key}");
        put("tr", "im_log_downloaded", "İndirildi: {key}");
        put("zh", "im_log_downloaded", "已下载：{key}");
        put("it", "im_log_downloaded", "Scaricato: {key}");
        put("ja", "im_log_downloaded", "ダウンロード済み: {key}");
        put("ko", "im_log_downloaded", "다운로드됨: {key}");
        put("nl", "im_log_downloaded", "Gedownload: {key}");
        put("cs", "im_log_downloaded", "Staženo: {key}");
        put("sv", "im_log_downloaded", "Nedladdad: {key}");
        put("vi", "im_log_downloaded", "Đã tải: {key}");
        put("id", "im_log_downloaded", "Diunduh: {key}");
        put("ar", "im_log_downloaded", "تم التنزيل: {key}");

        // im_log_error
        put("ru", "im_log_error", "Ошибка ({key}): {message}");
        put("en", "im_log_error", "Error ({key}): {message}");
        put("de", "im_log_error", "Fehler ({key}): {message}");
        put("fr", "im_log_error", "Erreur ({key}) : {message}");
        put("es", "im_log_error", "Error ({key}): {message}");
        put("pt", "im_log_error", "Erro ({key}): {message}");
        put("uk", "im_log_error", "Помилка ({key}): {message}");
        put("be", "im_log_error", "Памылка ({key}): {message}");
        put("kk", "im_log_error", "Қате ({key}): {message}");
        put("pl", "im_log_error", "Błąd ({key}): {message}");
        put("tr", "im_log_error", "Hata ({key}): {message}");
        put("zh", "im_log_error", "错误（{key}）：{message}");
        put("it", "im_log_error", "Errore ({key}): {message}");
        put("ja", "im_log_error", "エラー ({key}): {message}");
        put("ko", "im_log_error", "오류 ({key}): {message}");
        put("nl", "im_log_error", "Fout ({key}): {message}");
        put("cs", "im_log_error", "Chyba ({key}): {message}");
        put("sv", "im_log_error", "Fel ({key}): {message}");
        put("vi", "im_log_error", "Lỗi ({key}): {message}");
        put("id", "im_log_error", "Kesalahan ({key}): {message}");
        put("ar", "im_log_error", "خطأ ({key}): {message}");

        // im_log_installed
        put("ru", "im_log_installed", "Установлено: {key}");
        put("en", "im_log_installed", "Installed: {key}");
        put("de", "im_log_installed", "Installiert: {key}");
        put("fr", "im_log_installed", "Installé : {key}");
        put("es", "im_log_installed", "Instalado: {key}");
        put("pt", "im_log_installed", "Instalado: {key}");
        put("uk", "im_log_installed", "Встановлено: {key}");
        put("be", "im_log_installed", "Усталявана: {key}");
        put("kk", "im_log_installed", "Орнатылды: {key}");
        put("pl", "im_log_installed", "Zainstalowano: {key}");
        put("tr", "im_log_installed", "Kuruldu: {key}");
        put("zh", "im_log_installed", "已安装：{key}");
        put("it", "im_log_installed", "Installato: {key}");
        put("ja", "im_log_installed", "インストール済み: {key}");
        put("ko", "im_log_installed", "설치됨: {key}");
        put("nl", "im_log_installed", "Geïnstalleerd: {key}");
        put("cs", "im_log_installed", "Nainstalováno: {key}");
        put("sv", "im_log_installed", "Installerad: {key}");
        put("vi", "im_log_installed", "Đã cài đặt: {key}");
        put("id", "im_log_installed", "Terinstal: {key}");
        put("ar", "im_log_installed", "تم التثبيت: {key}");

        // im_log_install_error
        put("ru", "im_log_install_error", "Ошибка установки ({key}): {message}");
        put("en", "im_log_install_error", "Install error ({key}): {message}");
        put("de", "im_log_install_error", "Installationsfehler ({key}): {message}");
        put("fr", "im_log_install_error", "Erreur d'installation ({key}) : {message}");
        put("es", "im_log_install_error", "Error de instalación ({key}): {message}");
        put("pt", "im_log_install_error", "Erro de instalação ({key}): {message}");
        put("uk", "im_log_install_error", "Помилка встановлення ({key}): {message}");
        put("be", "im_log_install_error", "Памылка ўсталёўкі ({key}): {message}");
        put("kk", "im_log_install_error", "Орнату қатесі ({key}): {message}");
        put("pl", "im_log_install_error", "Błąd instalacji ({key}): {message}");
        put("tr", "im_log_install_error", "Kurulum hatası ({key}): {message}");
        put("zh", "im_log_install_error", "安装错误（{key}）：{message}");
        put("it", "im_log_install_error", "Errore di installazione ({key}): {message}");
        put("ja", "im_log_install_error", "インストールエラー ({key}): {message}");
        put("ko", "im_log_install_error", "설치 오류 ({key}): {message}");
        put("nl", "im_log_install_error", "Installatiefout ({key}): {message}");
        put("cs", "im_log_install_error", "Chyba instalace ({key}): {message}");
        put("sv", "im_log_install_error", "Installationsfel ({key}): {message}");
        put("vi", "im_log_install_error", "Lỗi cài đặt ({key}): {message}");
        put("id", "im_log_install_error", "Kesalahan instalasi ({key}): {message}");
        put("ar", "im_log_install_error", "خطأ في التثبيت ({key}): {message}");

        // im_status_done_error
        put("ru", "im_status_done_error", "Установка завершилась с ошибкой: {error}");
        put("en", "im_status_done_error", "Installation failed with error: {error}");
        put("de", "im_status_done_error", "Installation mit Fehler beendet: {error}");
        put("fr", "im_status_done_error", "L'installation s'est terminée avec une erreur : {error}");
        put("es", "im_status_done_error", "La instalación terminó con un error: {error}");
        put("pt", "im_status_done_error", "A instalação terminou com erro: {error}");
        put("uk", "im_status_done_error", "Встановлення завершилося з помилкою: {error}");
        put("be", "im_status_done_error", "Усталёўка завяршылася з памылкай: {error}");
        put("kk", "im_status_done_error", "Орнату қатемен аяқталды: {error}");
        put("pl", "im_status_done_error", "Instalacja zakończona błędem: {error}");
        put("tr", "im_status_done_error", "Kurulum hatayla sona erdi: {error}");
        put("zh", "im_status_done_error", "安装完成但出现错误：{error}");
        put("it", "im_status_done_error", "Installazione terminata con un errore: {error}");
        put("ja", "im_status_done_error", "インストールがエラーで終了しました: {error}");
        put("ko", "im_status_done_error", "설치가 오류와 함께 종료되었습니다: {error}");
        put("nl", "im_status_done_error", "Installatie voltooid met fout: {error}");
        put("cs", "im_status_done_error", "Instalace dokončena s chybou: {error}");
        put("sv", "im_status_done_error", "Installationen slutfördes med ett fel: {error}");
        put("vi", "im_status_done_error", "Cài đặt kết thúc với lỗi: {error}");
        put("id", "im_status_done_error", "Instalasi selesai dengan kesalahan: {error}");
        put("ar", "im_status_done_error", "اكتمل التثبيت مع وجود خطأ: {error}");

        // im_status_done_error_short
        put("ru", "im_status_done_error_short", "Завершено с ошибками");
        put("en", "im_status_done_error_short", "Finished with errors");
        put("de", "im_status_done_error_short", "Mit Fehlern abgeschlossen");
        put("fr", "im_status_done_error_short", "Terminé avec des erreurs");
        put("es", "im_status_done_error_short", "Finalizado con errores");
        put("pt", "im_status_done_error_short", "Concluído com erros");
        put("uk", "im_status_done_error_short", "Завершено з помилками");
        put("be", "im_status_done_error_short", "Завершана з памылкамі");
        put("kk", "im_status_done_error_short", "Қателермен аяқталды");
        put("pl", "im_status_done_error_short", "Zakończono z błędami");
        put("tr", "im_status_done_error_short", "Hatalarla tamamlandı");
        put("zh", "im_status_done_error_short", "已完成但有错误");
        put("it", "im_status_done_error_short", "Terminato con errori");
        put("ja", "im_status_done_error_short", "エラーで終了しました");
        put("ko", "im_status_done_error_short", "오류와 함께 완료됨");
        put("nl", "im_status_done_error_short", "Voltooid met fouten");
        put("cs", "im_status_done_error_short", "Dokončeno s chybami");
        put("sv", "im_status_done_error_short", "Slutfört med fel");
        put("vi", "im_status_done_error_short", "Hoàn tất với lỗi");
        put("id", "im_status_done_error_short", "Selesai dengan kesalahan");
        put("ar", "im_status_done_error_short", "اكتمل مع وجود أخطاء");

        // im_status_done_success
        put("ru", "im_status_done_success", "Установка завершена. Клиент готов к запуску");
        put("en", "im_status_done_success", "Installation complete. The client is ready to launch");
        put("de", "im_status_done_success", "Installation abgeschlossen. Der Client ist startbereit");
        put("fr", "im_status_done_success", "Installation terminée. Le client est prêt à être lancé");
        put("es", "im_status_done_success", "Instalación completa. El cliente está listo para iniciarse");
        put("pt", "im_status_done_success", "Instalação concluída. O cliente está pronto para iniciar");
        put("uk", "im_status_done_success", "Встановлення завершено. Клієнт готовий до запуску");
        put("be", "im_status_done_success", "Усталёўка завершана. Кліент гатовы да запуску");
        put("kk", "im_status_done_success", "Орнату аяқталды. Клиент іске қосуға дайын");
        put("pl", "im_status_done_success", "Instalacja zakończona. Klient jest gotowy do uruchomienia");
        put("tr", "im_status_done_success", "Kurulum tamamlandı. İstemci başlatılmaya hazır");
        put("zh", "im_status_done_success", "安装完成。客户端已准备好启动");
        put("it", "im_status_done_success", "Installazione completata. Il client è pronto per l'avvio");
        put("ja", "im_status_done_success", "インストールが完了しました。クライアントは起動準備が整っています");
        put("ko", "im_status_done_success", "설치가 완료되었습니다. 클라이언트를 실행할 준비가 되었습니다");
        put("nl", "im_status_done_success", "Installatie voltooid. De client is klaar om te starten");
        put("cs", "im_status_done_success", "Instalace dokončena. Klient je připraven ke spuštění");
        put("sv", "im_status_done_success", "Installationen är klar. Klienten är redo att starta");
        put("vi", "im_status_done_success", "Cài đặt hoàn tất. Ứng dụng khách đã sẵn sàng khởi chạy");
        put("id", "im_status_done_success", "Instalasi selesai. Klien siap dijalankan");
        put("ar", "im_status_done_success", "اكتمل التثبيت. العميل جاهز للتشغيل");
    }

    private static void put(String lang, String key, String value) {
        STRINGS.computeIfAbsent(lang, k -> new LinkedHashMap<>()).put(key, value);
    }

    public static String get(String lang, String key) {
        Map<String, String> map = STRINGS.get(lang);
        if (map == null) {
            map = STRINGS.get("en");
        }
        String value = map.get(key);
        if (value == null) {
            value = STRINGS.get("en").get(key);
        }
        return value;
    }

    public static String detectSystemLang() {
        String code = Locale.getDefault().getLanguage();
        for (Lang lang : SUPPORTED) {
            if (lang.code.equals(code)) {
                return code;
            }
        }
        return "en";
    }

    public static Lang findLang(String code) {
        for (Lang lang : SUPPORTED) {
            if (lang.code.equals(code)) {
                return lang;
            }
        }
        return SUPPORTED[1];
    }
}
