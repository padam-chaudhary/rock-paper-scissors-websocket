package com.game;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

public class RPSGameServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Create / get session
        HttpSession session = request.getSession(true);

        // Read parameters
        String player = request.getParameter("player");
        String choice = request.getParameter("choice");

        // Safety: default choice if none selected (timer auto-submit)
        if (choice == null || choice.trim().isEmpty()) {
            choice = "Rock";
        }

        // Store player choices
        if ("1".equals(player)) {
            session.setAttribute("p1", choice);

            // After Player 1 submits, wait for Player 2
            response.sendRedirect("player2.html");
            return;
        }

        if ("2".equals(player)) {
            session.setAttribute("p2", choice);
        }

        // Retrieve both choices
        String p1 = (String) session.getAttribute("p1");
        String p2 = (String) session.getAttribute("p2");

        // If both players have submitted
        if (p1 != null && p2 != null) {

            String result = decideWinner(p1, p2);

            // Send data to result.jsp
            request.setAttribute("p1", p1);
            request.setAttribute("p2", p2);
            request.setAttribute("result", result);

            // Clear session for next game
            session.invalidate();

            // Forward to result page
            RequestDispatcher rd = request.getRequestDispatcher("result.jsp");
            rd.forward(request, response);
        }
    }

    // Game logic
    private String decideWinner(String p1, String p2) {

        if (p1.equals(p2)) {
            return "Draw 🤝";
        }

        if (
            (p1.equals("Rock") && p2.equals("Scissors")) ||
            (p1.equals("Paper") && p2.equals("Rock")) ||
            (p1.equals("Scissors") && p2.equals("Paper"))
        ) {
            return "Player 1 Wins 🎉";
        }

        return "Player 2 Wins 🎉";
    }
}
