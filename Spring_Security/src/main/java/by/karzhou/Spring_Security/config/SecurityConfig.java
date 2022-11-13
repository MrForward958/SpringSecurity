package by.karzhou.Spring_Security.config;

import by.karzhou.Spring_Security.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


//https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter

//Главный класс где мы настраиваем наше Spring Security
//Этой аннотацией даем понять Spring, что это конфигурационный класс для Spring security
@EnableWebSecurity
//Этой аннотацией указываем что будем указывать доступ к методам через аннотацию @PreAuthorize
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //Внедряем наш сервис для сущности.
    private final PersonDetailsService personDetailsService;

    @Autowired
    public SecurityConfig(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    //Конфигурируем сам Spring security
    //Конфигурируем авторизацию
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //Все условия по авторизации читаются сверху в низ, как if - if - else.
        //Теперь все запросы в приложении будут проходить авторизацию .authorizeRequests().
        httpSecurity.authorizeRequests()
                //Если пользователь перешел по "/auth/login" адресу пускаем его(не аутентифицированного пользователя)
                //Так же пускаем его на страницу с ошибкой "/error"
                //permitAll - указываем что всех пускаем.
                .antMatchers("/auth/login","/error","/auth/registration").permitAll()
                //anyRequest().hasAnyRole - для всех остальных запросов доступ имеют admin and user
                .anyRequest().hasAnyRole("ADMIN","USER")
                //and() - после настройки авторизации настраиваем страницу логина.(разделительно слово типо)
                .and()
                //Указываем какую страницу для логина будем использовать, вместо страницы по умолчанию.
                .formLogin().loginPage("/auth/login")
                //Указываем адрес с формы, куда будет отправляться результат.
                //По этому адресу Spring Security будет ждать логин и пароль
                //Нам не нужно самим в контроллере обрабатывать эти данные с формы.
                .loginProcessingUrl("/process_login")
                //Здесь мы указываем что после успешной аутентификации,
                //хотим что бы Spring отправил нас на страницу "/hello".
                //True - что бы в любом случае после успешной аутентификации отправлял на указанную страницу.
                .defaultSuccessUrl("/hello",true)
                //В случае не верной аутентификации отправляем обратно на адрес "/auth/login".
                //И в параметрах ключ ?error, в форме проверяем его наличие.
                //В случаем обнаружения его выводиться ошибка.
                .failureUrl("/auth/login?error")
                //разделительно слово
                .and()
                //Указываем что будем настраивать logout
                .logout()
                //URL для logout
                .logoutUrl("/logout")
                //Куда перекинет после успешного logout.
                .logoutSuccessUrl("/auth/login");
    }

    //Внедряем аутентификацию (она будет выполнять при помощи сервиса)
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(personDetailsService)
                //Указываем что при аутентификации пароль тоже шифровать и сравнивать уже зашифрованный с зашифрованным в БД.
                .passwordEncoder(getPasswordEncoder());
    }

    //Здесь мы указываем как шифруем пароль (с помощью BCrypt)
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
