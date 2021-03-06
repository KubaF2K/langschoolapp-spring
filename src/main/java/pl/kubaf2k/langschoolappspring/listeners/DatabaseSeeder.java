package pl.kubaf2k.langschoolappspring.listeners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.kubaf2k.langschoolappspring.models.Course;
import pl.kubaf2k.langschoolappspring.models.Language;
import pl.kubaf2k.langschoolappspring.models.Role;
import pl.kubaf2k.langschoolappspring.models.User;
import pl.kubaf2k.langschoolappspring.repositories.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseSeeder {

    private final RoleRepository roleRepository;
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    //TODO constraints

    @Autowired
    public DatabaseSeeder(RoleRepository roleRepository,
                          LanguageRepository languageRepository,
                          UserRepository userRepository,
                          CourseRepository courseRepository,
                          StatusRepository statusRepository,
                          PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener
    public void seed(ContextRefreshedEvent event) {
        seedRoles();
        seedLanguages();
        seedUsers();
        seedCourses();
    }

    public void seedRoles() {

        if (roleRepository.findByName("ROLE_USER").isPresent())
            return;
        roleRepository.deleteAll();
        List<Role> roles = Arrays.asList(
                new Role("ROLE_USER"),
                new Role("ROLE_TEACHER"),
                new Role("ROLE_ADMIN")
        );
        roleRepository.saveAll(roles);
    }

    public void seedLanguages() {
        if (languageRepository.findByCode("EN").isPresent())
            return;
        languageRepository.deleteAll();
        List<Language> languages = Arrays.asList(
                new Language("EN", "Angielski", "J??zyk przydatny wsz??dzie"),
                new Language("PL", "Polski", "Nasza ojczysta mowa"),
                new Language("DE", "Niemiecki", "J??zyk s??siad??w z zachodu"),
                new Language("ES", "Hiszpa??ski", "J??zyk kraju flamenco"),
                new Language("FR", "Francuski", "Oficjalny j??zyk Unii Europejskiej")
        );
        languageRepository.saveAll(languages);
    }

    public void seedUsers() {
        if (userRepository.findByName("admin").isPresent())
            return;
        statusRepository.deleteAll();
        userRepository.deleteAll();
        Role admin = roleRepository.findByName("ROLE_ADMIN").orElseThrow();
        Role teacher = roleRepository.findByName("ROLE_TEACHER").orElseThrow();
        Role user = roleRepository.findByName("ROLE_USER").orElseThrow();
        List<User> users = Arrays.asList(
                new User(
                        "Administrator",
                        "Strony",
                        "admin",
                        "admin@email.com",
                        passwordEncoder.encode("12345678"),
                        admin
                ),
                new User(
                        "Robert",
                        "Smith",
                        "bob",
                        "bob@email.com",
                        passwordEncoder.encode("1234"),
                        languageRepository.findByCode("EN").orElseThrow(),
                        teacher
                ),
                new User(
                        "Ewa",
                        "Kowalska",
                        "ewa",
                        "ewa@email.com",
                        passwordEncoder.encode("1234"),
                        languageRepository.findByCode("PL").orElseThrow(),
                        teacher
                ),
                new User(
                        "Hans",
                        "Gruber",
                        "hans",
                        "hans@email.com",
                        passwordEncoder.encode("1234"),
                        languageRepository.findByCode("DE").orElseThrow(),
                        teacher
                ),
                new User(
                        "Roberto",
                        "Garcia",
                        "roberto",
                        "roberto@email.com",
                        passwordEncoder.encode("1234"),
                        languageRepository.findByCode("ES").orElseThrow(),
                        teacher
                ),
                new User(
                        "Jean",
                        "Martin",
                        "jean",
                        "jean@email.com",
                        passwordEncoder.encode("1234"),
                        languageRepository.findByCode("FR").orElseThrow(),
                        teacher
                ),
                new User(
                        "Jan",
                        "Kowalski",
                        "jan",
                        "jan@email.com",
                        passwordEncoder.encode("1234"),
                        user
                )
        );
        userRepository.saveAll(users);
    }

    public void seedCourses() {
        if (courseRepository.existsByName("Angielski podstawowy"))
            return;
        courseRepository.deleteAll();
        List<Course> courses = Arrays.asList(
                new Course(
                        "Angielski podstawowy",
                        5,
                        "Poznaj podstawy j??zyka kt??rym mo??na si?? dogada?? prawie wsz??dzie! Ten kurs " +
                                "uczy angielskiego od podstaw, w prosty spos??b nauczysz si?? s????w i zwrot??w " +
                                "przydatnych na co dzie??.",
                        new BigDecimal(550),
                        languageRepository.findByCode("EN").orElseThrow(),
                        userRepository.findByName("bob").orElseThrow()
                ),
                new Course(
                        "Angielski zaawansowany",
                        6,
                        "Ten kurs jest przeznaczony dla os??b kt??re chc?? udoskonali?? swoj?? znajomo???? " +
                                "j??zyka angielskiego. Celem kursu jest kszta??towanie umiej??tno??ci komunikacji oraz " +
                                "poznanie nowych zwrot??w i wyra??e??.",
                        new BigDecimal(800),
                        languageRepository.findByCode("EN").orElseThrow(),
                        userRepository.findByName("bob").orElseThrow()
                ),
                new Course(
                        "Polski dla obcokrajowc??w",
                        4,
                        "Je??li potrzebujesz pozna?? polski lepiej, ten kurs jest dla ciebie. Pomo??e ci " +
                                "szybko pozna?? polski w stopniu komunikatywnym.",
                        new BigDecimal(450),
                        languageRepository.findByCode("PL").orElseThrow(),
                        userRepository.findByName("ewa").orElseThrow()
                ),
                new Course(
                        "Niemiecki podstawowy",
                        5,
                        "Poznaj podstawy j??zyka s??siad??w z zachodu. Kurs opracowany jest z my??l?? o " +
                                "praktycznym zastosowaniu j??zyka.",
                        new BigDecimal(520),
                        languageRepository.findByCode("DE").orElseThrow(),
                        userRepository.findByName("hans").orElseThrow()
                ),
                new Course(
                        "Niemiecki zaawansowany",
                        6,
                        "Kurs niemiecki dla zaawansowanych umo??liwia opanowanie prawie 2000 nowych " +
                                "wyra??e?? i pozna?? bli??ej gramatyk??.",
                        new BigDecimal(780),
                        languageRepository.findByCode("DE").orElseThrow(),
                        userRepository.findByName("hans").orElseThrow()
                ),
                new Course(
                        "Hiszpa??ski podstawowy",
                        5,
                        "Poznaj podstawy hiszpa??skiego dzi??ki naszemu kursowi. W ??atwy spos??b mo??esz " +
                                "pozna?? 2500 s????w i opanowa?? podstawow?? komunikacj??.",
                        new BigDecimal(500),
                        languageRepository.findByCode("ES").orElseThrow(),
                        userRepository.findByName("roberto").orElseThrow()
                ),
                new Course(
                        "Hiszpa??ski zaawansowany",
                        6,
                        "Nasz kurs zaawansowanego hiszpa??skiego rozszerza nasz kurs podstawowy o 4000 s????w i " +
                                "wyra??e??, plus pozwala na nauk?? s??ownictwa u??ywanego na co dzie??.",
                        new BigDecimal(750),
                        languageRepository.findByCode("ES").orElseThrow(),
                        userRepository.findByName("roberto").orElseThrow()
                ),
                new Course(
                        "Francuski podstawowy",
                        5,
                        "Kurs francuskiego podstawowego pozwala na przyswojenie wymowy i podstawowego " +
                                "s??ownictwa potrzebnego w ??yciu codziennym.",
                        new BigDecimal(500),
                        languageRepository.findByCode("FR").orElseThrow(),
                        userRepository.findByName("jean").orElseThrow()
                ),
                new Course(
                        "Francuski zaawansowany",
                        6,
                        "Francuski zaawansowany utrwala materia?? poznany w kursie podstawowym oraz uczy " +
                                "3000 nowych wyra??e??, nastawionych na kultur?? i obyczaje.",
                        new BigDecimal(750),
                        languageRepository.findByCode("FR").orElseThrow(),
                        userRepository.findByName("jean").orElseThrow()
                )
        );
        courseRepository.saveAll(courses);
    }
}
