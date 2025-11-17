package com.blogapp.blogApp.service.impl;

import com.blogapp.blogApp.dto.UserCreateDto;
import com.blogapp.blogApp.dto.UserLoginDto;
import com.blogapp.blogApp.dto.UserResponseDto;
import com.blogapp.blogApp.entity.Role;
import com.blogapp.blogApp.entity.User;
import com.blogapp.blogApp.entity.UserRole;
import com.blogapp.blogApp.repository.UserRepository;
import com.blogapp.blogApp.repository.UserRoleRepository;
import com.blogapp.blogApp.service.RoleService;
import com.blogapp.blogApp.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

//Bu sınıf:
//Yeni kullanıcı kaydeder
//Şifreyi hash’ler
//USER rolünü otomatik atar
//Login sırasında kullanıcıyı doğrular
//Rollerle birlikte UserResponseDto döner


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;


    //kayıt register(Request DTO): veriyi alır, yeni kullanıcı oluşturur, kaydeder, rol atar ve UserResponseDto döner.
    //Girdi olarak UserCreateDto alır, yeni bir User oluşturur
    //override: metodun bir arayüzde (interface) tanımlanmış bir metodu uyguladığı anlamına gelir.
    //UserService arayüzündeki register() imzasını implemente ediyoruz
    //UserCreateDto--> Request DTO --> Kullanıcıdan gelen veriyi taşır
    //User --> Entity --> Veritabanı nesnesidir
    //UserResponseDto--> Response DTO--> Kullanıcıya gösterilecek cevaptır

    @Override
    @Transactional
    public UserResponseDto register(UserCreateDto request) {  // DTO → Entity → DTO akışı

        //Email kontrolü
        if(userRepository.existsByEmail(request.email())){
            throw new RuntimeException("Bu email zaten kayıtlı!");
        }

        //DTO → Entity (Frontend’ten gelen veriyi veritabanı nesnesine(User entity’sine) çeviriyorsun)
        //Kullanıcıyı oluştur ve kaydet
        //Şifre hash’lenir
        User user= User.builder()
                .userName(request.userName()) //-> kullanıcı adı set edilir. kullanıcı adını alırsın.
                .email(request.email()) // e-posta set edilir.email’e erişirsin.
                .password(passwordEncoder.encode(request.password())) //gelen düz metin şifre passwordEncoder ile encode/hashed ediliyor.(BCrypt)
                .build();

        user= userRepository.save(user); //User entity’si veritabanına kaydedilir. kaydedilen entity’yi geri döner

        // Default role -> USER  //Varsayılan rol ata
        Role role =roleService.getOrCreate("USER");

        //UserRole kullanıcı-rolleri ilişkilendirmek için kullanılan bir entity .Burada user ve role ilişkilendiriliyor
        UserRole userRole = UserRole.builder()
                .user(user)
                .role(role)
                .active(true)
                .build();

        //Kullanıcı-rol ilişkisi veritabanına kaydediliyor.
        userRoleRepository.save(userRole);


        //Entity → DTO (Cevap olarak dönüyorsun)
        //veritabanına kaydedilmiş entity’yi UserResponseDto'ya dönüştürüyorsun.
        //Şifre veya gizli alanlar yoktur
        return new UserResponseDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                Set.of("USER") //Set.of("USER") ile dönen roller immutable bir set olarak veriliyor.
        );
    }


    //giriş yapmak isteyen kullanıcının e-postası ve şifresini kontrol etmek
    // giriş başarılıysa kullanıcı bilgilerini (roller dahil) döndürmek
    //request nesnen UserLoginDto. Bu sadece login formundan gelen veriyi (email + password) tutar.
    //UserLoginDto içindeki email → User entity’sine ulaşmak için kullanılıyor.Request DTO → Entity (okuma yoluyla)


    @Override
    public UserResponseDto login(UserLoginDto request) {

        //veritabanında bu e-posta ile kayıtlı kullanıcıyı arar. Varsa user değişkenine atanır.Yoksa hata fırlat.
        //Request DTO -> Entity (email ile arama)
        // join fetch ile roller de yüklenmiş olur.
        User user = userRepository.findByEmailWithRoles(request.email())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        //Şifre doğru mu kontrolü
        //Kullanıcının girdiği ham şifre (request.password()) ile veritabanındaki şifrelenmiş(user.getPassword()) şifreyi karşılaştırır.
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Şifre hatalı!");
        }

        //UserRole üzerinden Role’a ulaşılır
        // Bu kullanıcının sahip olduğu rollerin isimlerini listele.

        Set<String> roles = user.getRoles() //kullanıcıya ait UserRole nesnelerini getirir.
                .stream()
                .map(ur -> ur.getRole().getRoleName())
                .collect(Collectors.toSet());

        //Kullanıcı bilgisi + roller dön
        //User (entity) → UserResponseDto (response DTO) dönüşümü (cevap olarak)
        return new UserResponseDto(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                roles
        );
    }
}
