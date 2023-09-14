# PollAndVote
A polling application that allows users to create, end, view polls made by others, and vote on them.

* Backend built with **Spring: Spring Boot, Spring Data JPA, Spring Security, Spring MVC**, deployed with AWS ECS/ECR/RDS
* Frontend built with **React, JS, HTML/CSS**, deployed with AWS Cloudfront/S3, used AWS Route 53 for DNS management

# How it works:

## 1. Register
If you have not already done so, register with your name, email and password 

## 2. Login
Login with the correct credentials

---

## View all polls
* Once you have logged in successfully, you will be able to see all ongoing polls and polls that have already ended
* Ongoing polls are in grey, and polls that have ended are in orange
* Click on a poll that you wish to see to either:
  1. Vote on it if it hasn't already ended. If you have already voted, you cannot change your vote, but you can see what you voted for
  2. See the poll results if the poll has already ended

## Create a poll
* In the dashboard, click the green button at the bottom right of the screen to create a poll
* You have to give at least two non-empty options for the poll.
* You can choose to allow for multiple options to be selectable in the poll

## End a poll
* You can only end the poll if you are the creator of the poll
* Go to the poll that you want to end, and click the "end poll" button

---

* <a href="https://www.flaticon.com/free-icons/poll" title="poll icons">Poll icons created by Md Tanvirul Haque - Flaticon</a>


