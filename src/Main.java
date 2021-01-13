import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static List<String> names = new ArrayList<>(); //список для хранения названий изображений

    public static void main(String[] args) throws IOException {
        List<String> links = new ArrayList<>();  //список для хранения ссылок на файлы
        String pathToSave = "data/images/"; //путь для сохранения файлов
        Document doct = Jsoup.connect("https://lenta.ru").get(); //подключаемся напрямую к сайту
        Elements elements = doct.select("img[src]");
        System.out.println("Список ссылок на изображения: ");
        elements.forEach(element -> {
            links.add(element.attr("abs:src")); //добавляем все полученные ссылки в список
            System.out.println(element.attr("abs:src")); //печатаем полученные ссылки
        });
        for (String link : links) {
            int index = link.lastIndexOf("/") + 1;
            int indexTwo = link.lastIndexOf(".");
            if (indexTwo - index > 0){
            names.add(link.substring(index, indexTwo));
            } else {
                names.add(link.substring(index));
            }
        }
        downloadFiles(links, pathToSave); //метод для скачивания файлов по ссылкам
    }

    private static void downloadFiles(List<String> links, String pathToSave) throws IOException {
        System.out.println("Сохранение файлов в указанный путь...");
        for (int i = 0; i < links.size(); i++) {
            URL url = new URL(links.get(i)); //Формирование url-адреса файла
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);

           try (BufferedInputStream bis = new BufferedInputStream(conn.getInputStream())) {
               try (FileOutputStream fos = new FileOutputStream(new File(pathToSave + names.get(i) + ".jpg"))) {

               int ch;
               while ((ch = bis.read()) != -1)  //чтение файла
               {
                   fos.write(ch); //запись в стрим
               }
               bis.close();
               fos.flush();

               }
           }
        }
        System.out.println("Успешно завершено!");
    }
}
