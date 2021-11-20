package uofg.zehuilyu.queueapi.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uofg.zehuilyu.queueapi.entity.ServiceWindow;
import uofg.zehuilyu.queueapi.repository.WindowRepository;

import java.util.Date;

@Api(value = "Service Window Management", tags = "Service Window Management API", description = "Service Window")
@RestController
@RequestMapping("/api/v1/window")
public class WindowController {
    @Autowired
    WindowRepository windowRepository;

    /**
     * Get all windows list.
     *
     * @return JSONObject of the list
     */
    @ApiOperation(value = "Get all windows list", notes = "GetAllWindows", response = JSONObject.class, produces = "application/json")
    @GetMapping("/windows")
    public JSONArray getAllWindows(){
        return (JSONArray) JSONObject.toJSON(windowRepository.findAll());
    }

    /**
     * Gets windows by id.
     *
     * @param windowId the user id
     * @return ResponseEntity with body: JSONObject of the window
     * @throws ResourceNotFoundException the resource not found exception
     */
    @ApiOperation(value = "Get window by id", notes = "GetWindowById", response = ResponseEntity.class, produces = "application/json")
    @GetMapping("/windows/{id}")
    public ResponseEntity<JSONObject> getWindowById(@PathVariable(value = "id") Integer windowId)
            throws ResourceNotFoundException {
        ServiceWindow window =
                windowRepository
                        .findById(windowId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + windowId));

        return ResponseEntity.ok().body((JSONObject) JSONObject.toJSON(window));
    }

    /**
     * Create a new Window
     * @param windowJSON the window JSONObject
     * @return JSONObject of the window
     */
    @ApiOperation(value = "Create a new window", notes = "CreateWindow", response = JSONObject.class, produces = "application/json")
    @PostMapping("/createwindow")
    public JSONObject createWindow(@Validated @RequestBody JSONObject windowJSON) {
        ServiceWindow newWindow =windowRepository.save(JSONObject.toJavaObject(windowJSON, ServiceWindow.class));
        return (JSONObject) JSONObject.toJSON(newWindow);
    }

    /**
     * Update Window response entity.
     * @param windowId the customer id
     * @param windowJSON the customer details JSONObject
     * @return the response entity
     * @throws ResourceNotFoundException
     */
    @ApiOperation(value = "Update a window", notes = "UpdateWindow", response = ResponseEntity.class, produces = "application/json")
    @PutMapping("/windows/{id}")
    public ResponseEntity<JSONObject> updateWindow(
            @PathVariable(value = "id") Integer windowId, @Validated @RequestBody JSONObject windowJSON)
            throws ResourceNotFoundException {

        ServiceWindow window =
                windowRepository
                        .findById(windowId)
                        .orElseThrow(() -> new ResourceNotFoundException("Window not found on :: " + windowId));

        ServiceWindow windowDetails=JSONObject.toJavaObject(windowJSON, ServiceWindow.class);
        window.setName(windowDetails.getName());
        window.setUpdate_time(new Date());
        window.setStatus(windowDetails.getStatus());
        final ServiceWindow updatedWindow = windowRepository.save(window);
        return ResponseEntity.ok((JSONObject) JSONObject.toJSON(updatedWindow));
    }


    /**
     * Delete window map.
     *
     * @param windowId the customer id
     * @return the JSONOject of successfully deleted.
     * @throws Exception the exception
     */
    @ApiOperation(value = "Delete a window", notes = "DeleteWindow", response = JSONObject.class, produces = "application/json")
    @DeleteMapping("/windows/{id}")
    public JSONObject deleteWindow(@PathVariable(value = "id") Integer windowId) throws Exception {
        ServiceWindow window =
                windowRepository
                        .findById(windowId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + windowId));

        windowRepository.delete(window);
        JSONObject response=new JSONObject();
        response.put("deleted",Boolean.TRUE);
        return response;
    }











}
