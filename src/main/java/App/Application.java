package App;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application {
    public static final String URL = "https://hh.ru/vacancies/programmist_java";
    public static void main(String[] args) throws IOException, NullPointerException {
        List<Vacancy> vacancies = parseVacancies(2);
        for (int i = 0; i < vacancies.size(); i++) {
            Vacancy vacancy = vacancies.get(i);
            System.out.printf("Вакансия #%d - %s\n", i + 1, vacancy.getLinkOfVacancy());
            System.out.println(vacancy);
            }
        }

    private static List<Vacancy> parseVacancies(int numberOfPages) throws IOException, NullPointerException {
        List<Vacancy> vacancies = new ArrayList<>();
        for (int i = 0; i < numberOfPages; i++) {
            Document document = Jsoup.connect(i == 0 ? URL : URL + "?page=" + i).get();
            parseVacancy(vacancies, document);
        }
        return vacancies;
    }

    private static void parseVacancy(List<Vacancy> vacancies, Document document) throws IOException, NullPointerException {
        Elements postTitleElements = document.getElementsByAttributeValue("data-qa", "serp-item__title");
        for (Element postTitleElement : postTitleElements) {
            String detailsLink = postTitleElement.attr("href");
            Vacancy vacancy = new Vacancy();
            vacancy.setLinkOfVacancy(detailsLink);
            vacancy.setTitle(postTitleElement.text());
            Document vacancyDetailsDocument = Jsoup.connect(detailsLink).get();

            parseExperience(vacancy, vacancyDetailsDocument);
            parseSkills(vacancy, vacancyDetailsDocument);
            parseAuthor(vacancy, vacancyDetailsDocument);
            parseDateOfCreated(vacancy, vacancyDetailsDocument);
            parseSalary(vacancy, vacancyDetailsDocument);

            vacancies.add(vacancy);
        }
    }

    private static void parseSalary(Vacancy vacancy, Document vacancyDetailsDocument) {
        try {
            vacancy.setSalary(getElementByAttribute(vacancyDetailsDocument));
        } catch (NullPointerException e) {
            vacancy.setSalary("Не указана");
        }
    }

    private static void parseExperience(Vacancy vacancy, Document vacancyDetailsDocument) {
        try {
            vacancy.setExperience(getElementByClass(vacancyDetailsDocument,"vacancy-description-list-item"));
        } catch (NullPointerException e) {
            vacancy.setExperience("Необходимый опыт не указан");
        }
    }

    private static void parseDateOfCreated(Vacancy vacancy, Document vacancyDetailsDocument) {
        try {
            vacancy.setDateOfCreated(getElementByClass(vacancyDetailsDocument,"vacancy-creation-time-redesigned"));
        } catch (NullPointerException e){
            vacancy.setDateOfCreated("Дата создания вакансии не указана");
        }
    }

    private static void parseAuthor(Vacancy vacancy, Document vacancyDetailsDocument) {
        try {
            vacancy.setAuthor(getElementByClass(vacancyDetailsDocument, "vacancy-company-name"));
        } catch (NullPointerException e) {
            vacancy.setAuthor("Не указан");
        }
    }

    private static void parseSkills(Vacancy vacancy, Document vacancyDetailsDocument) {
        Elements skillElements = vacancyDetailsDocument.getElementsByClass("bloko-tag__section bloko-tag__section_text");
        List<String> skills = new ArrayList<>();
        for (Element skillElement : skillElements) {
            String skill = skillElement.text();
            skills.add(skill);
        }
        vacancy.setSkills(skills);
    }

    private static String getElementByAttribute(Document document) {
        return document.getElementsByAttributeValue("data-qa", "vacancy-salary").first().text();
    }

    private static String getElementByClass(Document document, String className) {
        return document.getElementsByClass(className).first().text();
    }


}