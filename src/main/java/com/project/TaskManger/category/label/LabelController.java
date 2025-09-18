package com.project.TaskManger.category.label;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/labels")
@RequiredArgsConstructor
public class LabelController {
    private final LabelService labelService;
    @PostMapping
    public void addLabel(@RequestBody LabelDto request){
        labelService.addLabel(request);
    }
    @GetMapping("{category_id}")
    public List<Label> getLabels(@PathVariable("category_id") int category_id){
        return labelService.getLabels(category_id);
    }
    @GetMapping("/label/{label_id}")
    public Label getLabel(@PathVariable("label_id") int id){
        return labelService.getLabel(id);
    }
    @DeleteMapping("{label_id}")
    public void removeLabel(@PathVariable("label_id" )int id){
        labelService.removeLabel(id);
    }
    @PutMapping("{label_id}")
    public void updateLabel(@PathVariable("label_id")int id,@RequestBody LabelDto update){
        labelService.updateLabel(id, update);
    }
}
