package com.nalanbatters.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.nalanbatters.exception.UserNotFoundException;
import com.nalanbatters.model.NalanBattersModel;
import com.nalanbatters.model.User;
import com.nalanbatters.repository.NalanBattersRepo;
import com.nalanbatters.repository.UserRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NalanBattersService {
	@Autowired
    private JavaMailSender mailSender;
	@Autowired
	private NalanBattersRepo nalanBattersRepo;
	@Autowired
	private UserRepo userRepo;

	public void requestData(NalanBattersModel battersModel) throws MessagingException {
	    try {
	        int twoLtrTotal = battersModel.getTwoLtrQty() * battersModel.getTwoLtrPrice();
	        battersModel.setTwoLtrTotal(twoLtrTotal);
	        int fiveLtrTotal = battersModel.getFiveLtrQty() * battersModel.getFiveLtrPrice();
	        battersModel.setFiveLtrTotal(fiveLtrTotal);
	        int amount = twoLtrTotal + fiveLtrTotal;
	        battersModel.setAmount(amount);

	        // Save to database
	        try {
	            nalanBattersRepo.save(battersModel);
	        } catch (DataAccessException e) {
	            throw new RuntimeException("Failed to save order to the database: " + e.getMessage(), e);
	        }

	        // Send confirmation email
	        sendOrderConfirmationEmail(
	            battersModel.getUsername(),
	            battersModel.getEmail(),
	            battersModel.getTwoLtrQty(),
	            battersModel.getTwoLtrPrice(),
	            battersModel.getFiveLtrQty(),
	            battersModel.getFiveLtrPrice(),
	            twoLtrTotal,
	            fiveLtrTotal,
	            amount
	        );
	    } catch (MessagingException e) {
	        throw new MessagingException("Failed to send order confirmation email: " + e.getMessage(), e);
	    } catch (Exception e) {
	        throw new RuntimeException("An unexpected error occurred while processing the order: " + e.getMessage(), e);
	    }
	}


    public void sendOrderConfirmationEmail(String username,String tomail, int twoLtrQty,int twoLtrPrice, int fiveLtrQty, int fiveLtrPrice, int twoLtrTotal, int fiveLtrTotal, int amount) throws MessagingException {


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(tomail);
        helper.setSubject("Order Confirmation");

        String htmlContent = "<!DOCTYPE html>"
                +"<html lang=\"en\">"
                +"<head>"
                +    "<meta charset=\"UTF-8\">"
                +    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">"
                +    "<title>Order Confirmation</title>"
                +    "<style>"
                +        "body {"
                +            "font-family: Arial, sans-serif;"
                +            "margin: 0;"
                +            "padding: 0;"
                +            "background-color: #f9f9f9;"
                +        "}"
                +        ".email-container {"
                +            "max-width: 600px;"
                +            "margin: 20px auto;"
                +            "background: #ffffff;"
                +            "padding: 20px;"
                +            "border-radius: 8px;"
                +            "box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);"
                +        "}"
                +        ".header {"
                +            "text-align: center;"
                +            "padding: 10px 0;"
                +            "border-bottom: 2px solid #4CAF50;"
                +        "}"
                +        ".header h1 {"
                +            "margin: 0;"
                +            "font-size: 24px;"
                +            "color: #4CAF50;"
                +        "}"
                +        ".content {"
                +            "padding: 20px 0;"
                +        "}"
                +        ".content h2 {"
                +            "color: #333;"
                +            "font-size: 20px;"
                +        "}"
                +        ".content p {"
                +            "font-size: 16px;"
                +            "color: #555;"
                +            "line-height: 1.6;"
                +        "}"
                +        ".order-summary {"
                +            "margin: 20px 0;"
                +        "}"
                +        ".order-summary table {"
                +            "width: 100%;"
                +            "border-collapse: collapse;"
                +            "margin-bottom: 10px;"
                +        "}"
                +        ".order-summary th,"
                +        ".order-summary td {"
                +            "padding: 10px;"
                +            "text-align: left;"
                +            "border: 1px solid #ddd;"
                +        "}"
                +        ".order-summary th {"
                +            "background-color: #4CAF50;"
                +            "color: white;"
                +        "}"
                +        ".order-summary .total-row {"
                +            "font-weight: bold;"
                +        "}"
                +        ".footer {"
                +            "text-align: center;"
                +            "margin-top: 20px;"
                +            "font-size: 14px;"
                +            "color: #777;"
                +        "}"
                +        ".footer a {"
                +            "color: #4CAF50;"
                +            "text-decoration: none;"
                +        "}"
                +        "@media (max-width: 600px) {"
                +            ".header h1 {"
                +                "font-size: 20px;"
                +            "}"
                +            ".content p,"
                +            ".order-summary th,"
                +            ".order-summary td {"
                +                "font-size: 14px;"
                +            "}"
                +        "}"
                +    "</style>"
                +"</head>"
                +"<body>"
                +    "<div class=\"email-container\">"
                +        "<!-- Header -->"
                +        "<div class=\"header\">"
                +            "<h1>Order Confirmation</h1>"
                +            "<p>Thank you for your purchase!</p>"
                +        "</div>"
                +        "<!-- Content -->"
                +        "<div class=\"content\">"
                +            "<h2>Hi "+username.toUpperCase()+",</h2>"
                +            "<p>"
                +                "Your order has been successfully placed! Here are the details of your purchase:"
                +            "</p>"
                +        "</div>"
                +        "<!-- Order Summary -->"
                +        "<div class=\"order-summary\">"
                +            "<h2>Order Summary</h2>"
                +            "<table>"
                +                "<thead>"
                +                    "<tr>"
                +                        "<th>Item</th>"
                +                        "<th>Quantity</th>"
                +                        "<th>Price</th>"
                +                        "<th>Total</th>"
                +                    "</tr>"
                +                "</thead>"
                +                "<tbody>"
                +                    "<tr>"
                +                        "<td>2.5 Litters</td>"
                +                        "<td>"+twoLtrQty+"</td>"
                +                        "<td>&#36;"+twoLtrPrice+"</td>"
                +                        "<td>&#36;"+twoLtrTotal+"</td>"
                +                    "</tr>"
                +                    "<tr>"
                +                        "<td>5 Litters</td>"
                +                        "<td>"+fiveLtrQty+"</td>"
                +                        "<td>&#36;"+fiveLtrPrice+"</td>"
                +                        "<td>&#36;"+fiveLtrTotal+"</td>"
                +                    "</tr>"
                +                "</tbody>"
                +                "<tfoot>"
                +                    "<tr class=\"total-row\">"
                +                        "<td colspan=\"3\">Grand Total</td>"
                +                        "<td>"+amount+"</td>"
                +                    "</tr>"
                +                "</tfoot>"
                +            "</table>"
                +        "</div>"
                +        "<!-- Footer -->"
                +        "<div class=\"footer\">"
                +            "<p>"
                +                "Need help? Contact our support team at"
                +                "<a href=\"mailto:support@example.com\">support@example.com</a>."
                +            "</p>"
                +            "<p>"
                +                "Thank you for shopping with us! <br>"
                +                "<a href=\"https://example.com\">Visit our website</a>"
                +            "</p>"
                +        "</div>"
                +    "</div>"
                +"</body>"
                +"</html>";

        helper.setText(htmlContent, true);  // true indicates the message is in HTML

        mailSender.send(message);
    }


	public List<NalanBattersModel> getAllData() {
		
		return nalanBattersRepo.findAll();
		
	}


	public void validateUser(String email, String password) {
	    User user = userRepo.findByEmail(email)
	            .orElseThrow(() -> new UserNotFoundException("User not found"));

	    // Check if the password matches
	    if (!password.equals(user.getPassword())) {
	        throw new IllegalArgumentException("Invalid password");
	    }
	}
}
