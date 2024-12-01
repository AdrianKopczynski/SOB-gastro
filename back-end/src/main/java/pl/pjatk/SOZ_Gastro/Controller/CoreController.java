package pl.pjatk.SOZ_Gastro.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.pjatk.SOZ_Gastro.Services.CoreService;

@RestController
@RequestMapping("")
public class CoreController {
    private final CoreService coreService;

    public CoreController(CoreService coreService){this.coreService = coreService;}

    @GetMapping("/hello")
    public ResponseEntity<String> hello(@RequestParam(required=false)  String pin ){
        return ResponseEntity.ok(coreService.helloWorld()+pin);
    }

    @PostMapping("/log")
    public ResponseEntity<String> login(@RequestParam String pin){
        return ResponseEntity.ok(coreService.helloWorld()+pin);
    }
}
