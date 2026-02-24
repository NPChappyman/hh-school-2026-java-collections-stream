package tasks;

import common.Person;
import common.PersonService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);

    /*
    Кладём каждый элемент Set в Map, ключ используем id, а значение Person,
    с этим id. Для каждого элемента из persons будет вызван put(O(1)) в Map,
    значит для всего persons O(n)
     */
    Map<Integer,Person> mapPersons = persons.stream()
            .collect(Collectors.toMap(Person::id,Function.identity()));

    /*
    Заменяем каждый элемент из исходного списка, достав(O(1)) по ключу(id)-Person из mapPersons - O(n).
    Итоговая оценка асимптотики работы - O(n)
     */
    return personIds.stream()
              .map(mapPersons::get)
              .collect(Collectors.toList());
  }
}
