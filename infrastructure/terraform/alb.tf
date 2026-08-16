# Public Application Load Balancer.
resource "aws_lb" "main" {
  name               = "shopping-app-alb"
  internal           = false   #this is public if its true then means private
  load_balancer_type = "application"
  # ALB lives in public subnets.
  subnets = [
    aws_subnet.public.id,
    aws_subnet.public_2.id
  ]
  security_groups = [
    aws_security_group.alb.id
  ]
  tags = {
    Name = "shopping-app-alb"
  }
}

# ALB listener accepting browser HTTP requests.
resource "aws_lb_listener" "eureka" {
  load_balancer_arn = aws_lb.main.arn
  port              = 80
  protocol          = "HTTP"
  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.eureka.arn
  }
}
