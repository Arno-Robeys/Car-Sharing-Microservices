package be.ucll.apigateway.web;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import be.ucll.apigateway.api.TestApiDelegate;

@Component
public class ApiGatewayController implements TestApiDelegate {

    @Override
    public ResponseEntity<Void> test()
    {
        //Met deze methode kan je testen of de api-gateway werkt
        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }
}
