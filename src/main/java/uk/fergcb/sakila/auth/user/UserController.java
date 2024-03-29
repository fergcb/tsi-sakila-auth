package uk.fergcb.sakila.auth.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.fergcb.sakila.auth.Options;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userinfo/{userId}")
    public ResponseEntity<User> getUserInfo(@PathVariable String userId) {
        final User user = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> createUser(@RequestBody CredentialsDTO data) {
        userService.createUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(method = {RequestMethod.OPTIONS, RequestMethod.GET})
    public ResponseEntity<Options> getOptions() {
        final Options options = new Options();

        options.add(linkTo(UserController.class).withSelfRel().expand());
        options.add(linkTo(methodOn(UserController.class).createUser(null)).withRel("signup").expand());

        return ResponseEntity.ok(options);
    }

}
