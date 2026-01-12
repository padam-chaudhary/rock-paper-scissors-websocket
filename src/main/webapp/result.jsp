<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Game Result</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        /* General reset */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Arial', sans-serif;
        }

        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background: linear-gradient(135deg, #6a11cb, #2575fc);
            color: #fff;
        }

        .container {
            background-color: rgba(0, 0, 0, 0.7);
            padding: 30px 40px;
            border-radius: 15px;
            text-align: center;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.5);
            width: 90%;
            max-width: 400px;
        }

        h2 {
            margin-bottom: 20px;
            font-size: 2rem;
            letter-spacing: 1px;
        }

        p {
            font-size: 1.2rem;
            margin: 10px 0;
        }

        h3 {
            margin: 20px 0;
            font-size: 1.5rem;
            color: #ffd700;
        }

        a {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 25px;
            background-color: #ffd700;
            color: #000;
            text-decoration: none;
            font-weight: bold;
            border-radius: 50px;
            transition: all 0.3s ease;
        }

        a:hover {
            background-color: #ffea00;
            transform: scale(1.05);
        }

        @media (max-width: 480px) {
            .container {
                padding: 20px;
            }
            h2 {
                font-size: 1.5rem;
            }
            p {
                font-size: 1rem;
            }
            h3 {
                font-size: 1.2rem;
            }
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Game Result</h2>
    <p>Player 1: ${p1}</p>
    <p>Player 2: ${p2}</p>
    <h3>${result}</h3>
    <a href="player1.html">Play Again</a>
</div>

</body>
</html>
