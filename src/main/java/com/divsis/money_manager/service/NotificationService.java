package com.divsis.money_manager.service;

import com.divsis.money_manager.dto.ExpenseDTO;
import com.divsis.money_manager.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final ProfileRepository profileRepository;
    private final EmailService emailService;
    private final ExpenseService expenseService;
    @Value("${money.manager.frontend.url}")
    private String frontEndUrl;

    @Scheduled(cron = "0 0 22 * * *", zone = "IST")
    public void sendDailyIncomeExpenseReminder() {
        log.info("Job Started :sendDailyIncomeExpenseReminder");
        profileRepository.findAll().forEach(profile -> {
            String body = "<div style=\"background-color:#f4f7f6; padding:40px 20px; font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\">"
                    + "<div style=\"max-width:600px; margin:0 auto; background-color:#ffffff; border-radius:8px; overflow:hidden; box-shadow:0 4px 15px rgba(0,0,0,0.05);\">"
                    + "<div style=\"background-color:#1976d2; height:6px; width:100%;\"></div>"
                    + "<div style=\"padding:40px 32px;\">"
                    + "<h2 style=\"margin-top:0; color:#2c3e50; font-size:22px; font-weight:600;\">Hi " + profile.getName() + ",</h2>"
                    + "<p style=\"color:#555555; font-size:16px; line-height:1.6; margin-bottom:24px;\">"
                    + "This is a friendly reminder that you haven't logged any expenses or incomes for today. Keeping your records up to date helps you stay on top of your financial goals!"
                    + "</p>"
                    + "<div style=\"text-align:center; margin:32px 0;\">"
                    + "<a href=\"" + frontEndUrl + "\" style=\"display:inline-block; padding:14px 32px; background-color:#1976d2; color:#ffffff; text-decoration:none; border-radius:6px; font-size:16px; font-weight:bold; letter-spacing:0.5px;\">"
                    + "Log Today's Activity"
                    + "</a>"
                    + "</div>"
                    + "<p style=\"color:#777777; font-size:15px; line-height:1.5; margin-bottom:0;\">"
                    + "Best Regards,<br><br>"
                    + "<strong style=\"color:#2c3e50;\">Money Manager Team</strong>"
                    + "</p>"
                    + "</div>"
                    + "</div>"
                    + "</div>";
            emailService.sendEmail(profile.getEmail(), "Daily Reminder: Add Your Expenses and Incomes", body);
        });
        log.info("Job Ended :sendDailyIncomeExpenseReminder");
    }

    @Scheduled(cron = "0 0 23 * * *", zone = "IST")
    public void sendDailyExpenseSummary() {
        log.info("Job Started: sendDailyExpenseSummary");
        ZoneId zone = ZoneId.of("Asia/Kolkata");
        LocalDate today = LocalDate.now(zone);

        profileRepository.findAll().forEach(profile -> {
            List<ExpenseDTO> expenses = expenseService.getExpensesforUserOnDate(profile.getId(), today);

            if (!expenses.isEmpty()) {
                String emailContent = buildExpenseTable(expenses);
                emailService.sendEmail(profile.getEmail(), "Your Daily Expense Summary", emailContent);
            }
        });
        log.info("Job Ended: sendDailyExpenseSummary");
    }

    private String buildExpenseTable(List<ExpenseDTO> expenses) {
        StringBuilder html = new StringBuilder();

        // Internal CSS for a cleaner look
        html.append("<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto;'>")
                .append("<h2 style='color: #1976d2;'>Daily Expense Summary</h2>")
                .append("<table style='width: 100%; border-collapse: collapse; border-radius: 8px; overflow: hidden; box-shadow: 0 0 20px rgba(0,0,0,0.1);'>")
                .append("<thead>")
                .append("  <tr style='background-color: #1976d2; color: #ffffff; text-align: left;'>")
                .append("    <th style='padding: 12px 15px;'>Name</th>")
                .append("    <th style='padding: 12px 15px;'>Category</th>")
                .append("    <th style='padding: 12px 15px;'>Amount</th>")
                .append("  </tr>")
                .append("</thead>")
                .append("<tbody>");

        BigDecimal totalExpense = BigDecimal.ZERO; // Variable to accumulate the total amount

        for (int i = 0; i < expenses.size(); i++) {
            ExpenseDTO expense = expenses.get(i);

            // Add current expense amount to the total
            // Note: If getAmount() returns a BigDecimal, change this to: totalExpense = totalExpense.add(expense.getAmount());
            totalExpense = totalExpense.add(expense.getAmount() != null ? expense.getAmount() : BigDecimal.ZERO);

            // Zebra striping for readability
            String bgColor = (i % 2 == 0) ? "#ffffff" : "#f8f9fa";
            String category = (expense.getCategoryName() != null) ? expense.getCategoryName() : "N/A";

            html.append("<tr style='background-color: ").append(bgColor).append("; border-bottom: 1px solid #dddddd;'>")
                    .append("  <td style='padding: 12px 15px;'>").append(expense.getName()).append("</td>")
                    .append("  <td style='padding: 12px 15px;'><span style='background: #e3f2fd; color: #1976d2; padding: 4px 8px; border-radius: 4px; font-size: 0.85em;'>").append(category).append("</span></td>")
                    .append("  <td style='padding: 12px 15px; font-weight: bold;'>₹").append(expense.getAmount()).append("</td>")
                    .append("</tr>");
        }

        // Add the final Total row
        html.append("<tr style='background-color: #f1f5f9; border-top: 2px solid #1976d2;'>")
                .append("  <td colspan='2' style='padding: 12px 15px; text-align: right; font-weight: bold; color: #333333;'>Total Expense:</td>")
                .append("  <td style='padding: 12px 15px; font-weight: bold; color: #d32f2f;'>₹").append(totalExpense).append("</td>")
                .append("</tr>");

        html.append("</tbody></table>")
                .append("<p style='margin-top: 20px; font-size: 0.9em; color: #666;'>Regards,<br>Money Manager Team</p>")
                .append("</div>");

        return html.toString();
    }
}
