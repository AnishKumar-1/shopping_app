resource "aws_security_group" "alb_sg" {
  name        = "shopping-alb-sg"
  description = "Security group for application load balancer"
  vpc_id      = aws_vpc.shopping_vpc.id

  //security for incoming request from user/browser
  ingress {
    description = "Allow HTTP traffic from internet"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] //any public ip not fixed
  }

  //egress rule for outgoing traffic like where my service can reach
  //along with protocol = "-1", AWS interprets it as all ports, not just port 0.
  // if we don't define it alb automatically allow to go the traffic back
  egress {
    description = "Allow outbound traffic"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "shopping-alb-sg"
  }
}




//Actual ALB which will use security group as firewall and its listener
# resource "aws_alb" "shopping-alb" {
#   name               = "shopping-alb"
#   internal           = false //internal = false means your Application Load Balancer will be publicly accessible from the internet.
#   load_balancer_type = "application"
#   ip_address_type    = "ipv4"
#
#   security_groups = [
#     aws_security_group.alb_sg.id
#   ]
#
#   subnets = [
#     aws_subnet.public_subnet.id,
#     aws_subnet.public_subnet_2.id
#   ]
#
#   enable_deletion_protection = false //Allows Terraform to delete it later
#
#   tags = {
#     Name = "shopping-alb"
#   }
#
# }


//alb listener which listens the incoming request and then decides what to and then forward to the your service target group
# resource "aws_lb_listener" "alb_listener" {
#   load_balancer_arn = aws_alb.shopping-alb.arn
#   port              = 80
#   protocol          = "HTTP"
#
#   default_action {
#     type             = "forward"
#     target_group_arn = aws_lb_target_group.product_tg.arn
#   }
# }


//our product service target group
resource "aws_lb_target_group" "product_tg" {
  name     = "product-service-tg"
  port     = 8080
  protocol = "HTTP"
  vpc_id   = aws_vpc.shopping_vpc.id

  health_check {
    enabled             = true
    path                = "/actuator/health"
    protocol            = "HTTP"
    matcher             = "200"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 3
  }

  tags = {
    Name = "product-service-tg"
  }

}