# ECS Cluster
# The cluster is the logical place where our ECS services/tasks run.
resource "aws_ecs_cluster" "main" {
  name = "shopping-app-cluster"
  tags = {
    Name = "shopping-app-cluster"
  }
}


# Defines HOW ECS should run the Eureka container.
resource "aws_ecs_task_definition" "eureka" {

  # Name/family of this task definition
  family = "shopping-eureka"

  # Run this task using AWS Fargate
  requires_compatibilities = ["FARGATE"]

  # Fargate uses the task's own VPC network interface
  network_mode = "awsvpc"

  # 0.25 vCPU
  cpu = "256"

  # 512 MB memory
  memory = "512"

  # IAM role ECS uses to pull the image from ECR
  execution_role_arn = aws_iam_role.ecs_task_execution.arn

  # Container configuration
  container_definitions = jsonencode([
    {
      # Name of the container inside the ECS task
      name = "eureka"

      # Docker image that ECS should pull from ECR
      image = "${aws_ecr_repository.eureka.repository_url}:latest"

      # If this container stops, the task is considered stopped
      essential = true

      # Port exposed by the Eureka container
      portMappings = [
        {
          containerPort = 8761
          protocol      = "tcp"
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.eureka.name
          "awslogs-region"        = "ap-south-1"
          "awslogs-stream-prefix" = "eureka"
        }
      }
    }
  ])

  # AWS tag
  tags = {
    Name = "shopping-eureka-task"
  }
}


# CloudWatch Log Group for Eureka container logs.
# ECS will send the application's stdout/stderr here.
resource "aws_cloudwatch_log_group" "eureka" {

  # Name of the log group in CloudWatch
  name = "/ecs/shopping-eureka"

  # Keep logs for 7 days.
  # This prevents old logs from accumulating unnecessarily.
  retention_in_days = 7

  tags = {
    Name = "shopping-eureka-logs"
  }
}

# ECS Service keeps the desired number of Eureka tasks running
# inside our ECS cluster.
resource "aws_ecs_service" "eureka" {
  # Name of the ECS service
  name = "shopping-eureka-service"
  # ECS cluster where the task will run
  cluster = aws_ecs_cluster.main.id
  # Task Definition that describes how to run Eureka
  task_definition = aws_ecs_task_definition.eureka.arn
  # Keep 1 Eureka task running
  desired_count = 0
  # Use AWS Fargate to run the task
  launch_type = "FARGATE"
  # Network configuration for the ECS task
  network_configuration {
    # Run Eureka in the private subnet
    subnets = [aws_subnet.private.id]
    # Security Group attached to the Eureka task
    security_groups = [
      aws_security_group.ecs.id
    ]
    # Do NOT give the task a public IP
    assign_public_ip = false
  }
  load_balancer {
    container_name   = "eureka"
    container_port   = 8761
    target_group_arn = aws_lb_target_group.eureka.arn
  }
  tags = {
    Name = "shopping-eureka-service"
  }
  depends_on = [
    aws_lb_listener.eureka
  ]
}


# Target Group for Eureka.
# The ALB will forward requests to ECS tasks registered in this target group.
resource "aws_lb_target_group" "eureka" {
  name        = "shopping-eureka-tg"
  port        = 8761
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = aws_vpc.main.id

  # Eureka should return HTTP 200 on this endpoint when healthy.
  health_check {
    path                = "/"
    protocol            = "HTTP"
    port                = "8761"
    healthy_threshold   = 2
    unhealthy_threshold = 3
    timeout             = 5
    interval            = 30
  }
  tags = {
    Name = "shopping-eureka-target-group"
  }
}