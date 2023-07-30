package gunjoon98.scoreboard.web.contoller;

import gunjoon98.scoreboard.domain.service.AdminService;
import gunjoon98.scoreboard.web.form.save.DashBoardSaveForm;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/admin/dashboard")
    public void createDashBoard(@Validated @RequestBody DashBoardSaveForm dashBoardSaveForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            System.out.println("true");
        }

        System.out.println(dashBoardSaveForm.getStartDate().toString());
    }
}
