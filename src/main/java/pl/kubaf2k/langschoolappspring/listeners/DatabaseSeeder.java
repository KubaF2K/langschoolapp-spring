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
import pl.kubaf2k.langschoolappspring.repositories.CourseRepository;
import pl.kubaf2k.langschoolappspring.repositories.LanguageRepository;
import pl.kubaf2k.langschoolappspring.repositories.RoleRepository;
import pl.kubaf2k.langschoolappspring.repositories.UserRepository;

import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseSeeder {

    private final RoleRepository roleRepository;
    private final LanguageRepository languageRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    //TODO constraints

    @Autowired
    public DatabaseSeeder(RoleRepository roleRepository, LanguageRepository languageRepository, UserRepository userRepository, CourseRepository courseRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.languageRepository = languageRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
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

        if (roleRepository.findByName("ROLE_USER") != null)
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
        if (languageRepository.findByCode("EN") != null)
            return;
        languageRepository.deleteAll();
        List<Language> languages = Arrays.asList(
                new Language("EN", "Angielski", "Język przydatny wszędzie"),
                new Language("PL", "Polski", "Nasza ojczysta mowa"),
                new Language("DE", "Niemiecki", "Język sąsiadów z zachodu"),
                new Language("ES", "Hiszpański", "Język kraju flamenco"),
                new Language("FR", "Francuski", "Oficjalny język Unii Europejskiej")
        );
        languageRepository.saveAll(languages);
    }

    public void seedUsers() {
        if (userRepository.findByName("admin") != null)
            return;
        userRepository.deleteAll();
        Role admin = roleRepository.findByName("ROLE_ADMIN");
        Role teacher = roleRepository.findByName("ROLE_TEACHER");
        Role user = roleRepository.findByName("ROLE_USER");
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
                        languageRepository.findByCode("EN"),
                        teacher
                ),
                new User(
                        "Ewa",
                        "Kowalska",
                        "ewa",
                        "ewa@email.com",
                        passwordEncoder.encode("1234"),
                        languageRepository.findByCode("PL"),
                        teacher
                ),
                new User(
                        "Hans",
                        "Gruber",
                        "hans",
                        "hans@email.com",
                        passwordEncoder.encode("1234"),
                        languageRepository.findByCode("DE"),
                        teacher
                ),
                new User(
                        "Roberto",
                        "Garcia",
                        "roberto",
                        "roberto@email.com",
                        passwordEncoder.encode("1234"),
                        languageRepository.findByCode("ES"),
                        teacher
                ),
                new User(
                        "Jean",
                        "Martin",
                        "jean",
                        "jean@email.com",
                        passwordEncoder.encode("1234"),
                        languageRepository.findByCode("FR"),
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
        if (courseRepository.existsById(1))
            return;
        courseRepository.deleteAll();
        List<Course> courses = Arrays.asList(
                new Course(
                        "Angielski podstawowy",
                        5,
                        "Poznaj podstawy języka którym można się dogadać prawie wszędzie! Ten kurs " +
                                "uczy angielskiego od podstaw, w prosty sposób nauczysz się słów i zwrotów " +
                                "przydatnych na co dzień.",
                        new BigDecimal(550),
                        languageRepository.findByCode("EN"),
                        userRepository.findByName("bob")
                ),
                new Course(
                        "Angielski zaawansowany",
                        6,
                        "Ten kurs jest przeznaczony dla osób które chcą udoskonalić swoją znajomość " +
                                "języka angielskiego. Celem kursu jest kształtowanie umiejętności komunikacji oraz " +
                                "poznanie nowych zwrotów i wyrażeń.",
                        new BigDecimal(800),
                        languageRepository.findByCode("EN"),
                        userRepository.findByName("bob")
                ),
                new Course(
                        "Polski dla obcokrajowców",
                        4,
                        "Jeśli potrzebujesz poznać polski lepiej, ten kurs jest dla ciebie. Pomoże ci " +
                                "szybko poznać polski w stopniu komunikatywnym.",
                        new BigDecimal(450),
                        languageRepository.findByCode("PL"),
                        userRepository.findByName("ewa")
                ),
                new Course(
                        "Niemiecki podstawowy",
                        5,
                        "Poznaj podstawy języka sąsiadów z zachodu. Kurs opracowany jest z myślą o " +
                                "praktycznym zastosowaniu języka.",
                        new BigDecimal(520),
                        languageRepository.findByCode("DE"),
                        userRepository.findByName("hans")
                ),
                new Course(
                        "Niemiecki zaawansowany",
                        6,
                        "Kurs niemiecki dla zaawansowanych umożliwia opanowanie prawie 2000 nowych " +
                                "wyrażeń i poznać bliżej gramatykę.",
                        new BigDecimal(780),
                        languageRepository.findByCode("DE"),
                        userRepository.findByName("hans")
                ),
                new Course(
                        "Hiszpański podstawowy",
                        5,
                        "Poznaj podstawy hiszpańskiego dzięki naszemu kursowi. W łatwy sposób możesz " +
                                "poznać 2500 słów i opanować podstawową komunikację.",
                        new BigDecimal(500),
                        languageRepository.findByCode("ES"),
                        userRepository.findByName("roberto")
                ),
                new Course(
                        "Hiszpański zaawansowany",
                        6,
                        "Nasz kurs zaawansowanego hiszpańskiego rozszerza nasz kurs podstawowy o 4000 słów i " +
                                "wyrażeń, plus pozwala na naukę słownictwa używanego na co dzień.",
                        new BigDecimal(750),
                        languageRepository.findByCode("ES"),
                        userRepository.findByName("roberto")
                ),
                new Course(
                        "Francuski podstawowy",
                        5,
                        "Kurs francuskiego podstawowego pozwala na przyswojenie wymowy i podstawowego " +
                                "słownictwa potrzebnego w życiu codziennym.",
                        new BigDecimal(500),
                        languageRepository.findByCode("FR"),
                        userRepository.findByName("jean")
                ),
                new Course(
                        "Francuski zaawansowany",
                        6,
                        "Francuski zaawansowany utrwala materiał poznany w kursie podstawowym oraz uczy " +
                                "3000 nowych wyrażeń, nastawionych na kulturę i obyczaje.",
                        new BigDecimal(750),
                        languageRepository.findByCode("FR"),
                        userRepository.findByName("jean")
                )
        );
        courseRepository.saveAll(courses);
    }
}
