package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    if (persons.size() == 0) {
      return Collections.emptyList();
    }
    //Вместо того, чтобы менять исходной список, можем пропустить первый элемент с помощью skip
    return persons.stream().skip(1).map(Person::firstName).collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)

  //Улучшается читаемость, сокращаем код - теперь нет необходимости в накладных расходах по созданию stream,
  // а distinct и так использует хеш таблицы в своей реализации.
  public Set<String> getDifferentNames(List<Person> persons) {
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
//Исправлена логика работы - теперь ФИО верно склеивается(secondname не повторяется дважды)
// Код стал более читаемым и коротким.
    return List.of(person.firstName(),person.middleName(),person.secondName()).stream()
            .filter(personStr -> personStr != null)
            .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  //HashMap, который инициализирован с ёмкостью 1 будет расширяться и перераспределять элементы слишком часто - при добавлении
  //новых людей. Релизация с циклом for заменена на stream - улучшилась читаемость .
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {

    return persons.stream()
            .collect(Collectors.toMap(
                    Person::id,
                    person->convertPersonToString(person),
                    (previous,New)->previous
            ));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  //Прежний алгоритм выполняется за O(n*m) - вложенный цикл. Это плохой алгоритм.
  //Можно создать set из элементов одной из коллекций, а затем итерироваться по элементам другой, проверяя на совпадение
  //с каким-либо элементом из Set - это приемлимо, так как поиск в Set O(1).
  //Итоговая сложность - O(m) (на создание Map) + O(n) -> O(n+m)
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    Set<Person> person2Set = new HashSet<>(persons2);
    return persons1.stream().anyMatch(person2Set::contains);
  }

  // Посчитать число четных чисел
  //Упростили код - код стал более читаемым и простым. Для ясности использовали специальный метод count()
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(number->number%2==0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке

  //Хеш код обьекта Integer = самому значению числа. Так как мы инициализировали set cразу необходимым capacity, каждый
  //Integer будет расположен в бакете с индексом равным значению этого Integer.
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
