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

    public static void main(String[] args) throws IOException {
        List<Vacancy> vacancies = parseVacancies();
        for (int i = 0; i < vacancies.size(); i++) {
            Vacancy vacancy = vacancies.get(i);
            System.out.printf("Вакансия #%d - %s\n", i + 1, vacancy.getLinkOfVacancy());
            System.out.println(vacancy);
        }
    }

    private static List<Vacancy> parseVacancies() throws IOException {
        List<Vacancy> vacancies = new ArrayList<>();
        int numberOfPages = 2;
        for (int i = 0; i < numberOfPages; i++) {
            Document document = Jsoup.connect(i == 0 ? URL : URL + "?page=" + i).get();
            Elements postTitleElements = document.getElementsByAttributeValue("data-qa", "serp-item__title");

            for (Element postTitleElement : postTitleElements) {
                String detailsLink = postTitleElement.attr("href");
                Vacancy vacancy = new Vacancy();
                vacancy.setLinkOfVacancy(detailsLink);
                vacancy.setTitle(postTitleElement.text());
                Document vacancyDetailsDocument = Jsoup.connect(detailsLink).get();
                try {
                    vacancy.setExperience(vacancyDetailsDocument.getElementsByClass("vacancy-description-list-item").first().text());
                } catch (NullPointerException e) {
                    vacancy.setExperience("Не указан");
                }

                Elements skillElements = vacancyDetailsDocument.getElementsByClass("bloko-tag__section bloko-tag__section_text");
                List<String> skills = new ArrayList<>();
                for (Element skillElement : skillElements) {
                    String skill = skillElement.text();
                    skills.add(skill);
                }
                vacancy.setSkills(skills);

                vacancy.setAuthor(vacancyDetailsDocument.getElementsByClass("vacancy-company-name").first().text());

                vacancy.setDateOfCreated(vacancyDetailsDocument.getElementsByClass("vacancy-creation-time-redesigned").first().text());
                try {
                    vacancy.setSalary(vacancyDetailsDocument.getElementsByAttributeValue("data-qa", "vacancy-salary").first().text());
                } catch (NullPointerException e) {
                    vacancy.setSalary("Не указана");
                }
                vacancies.add(vacancy);
            }
        }
        return vacancies;
    }

}
