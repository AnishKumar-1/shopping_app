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

  # Keep logs for 1 days.
  # This prevents old logs from accumulating unnecessarily.
  retention_in_days = 1

  tags = {
    Name = "shopping-eureka-logs"
  }
}

#loggin for gateway
resource "aws_cloudwatch_log_group" "gateway" {

  # Name of the log group in CloudWatch
  name = "/ecs/shopping-gateway"

  # Keep logs for  1 days.
  # This prevents old logs from accumulating unnecessarily.
  retention_in_days = 1

  tags = {
    Name = "shopping-gateway-logs"
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
  desired_count = 1
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
  service_registries {
    registry_arn = aws_service_discovery_service.eureka.arn
  }
  tags = {
    Name = "shopping-eureka-service"
  }
}

# Target Group for gateway.
# The ALB will forward requests to ECS tasks registered in this target group.
# resource "aws_lb_target_group" "gateway" {
#   name        = "shopping-gateway-tg"
#   port        = 9090
#   protocol    = "HTTP"
#   target_type = "ip"
#   vpc_id      = aws_vpc.main.id
#
#   # Eureka should return HTTP 200 on this endpoint when healthy.
#   health_check {
#     path                = "/"
#     protocol            = "HTTP"
#     port                = "9090"
#     healthy_threshold   = 2
#     unhealthy_threshold = 3
#     timeout             = 5
#     interval            = 30
#   }
#   tags = {
#     Name = "shopping-gateway-target-group"
#   }
# }

# Target Group for Eureka.
# The ALB will forward requests to ECS tasks registered in this target group.

resource "aws_lb_target_group" "gateway" {
  name        = "shopping-gateway-tg"
  port        = 9090
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = aws_vpc.main.id

  # Gateway health check
  health_check {
    path                = "/actuator/health"
    protocol            = "HTTP"
    port                = "traffic-port"
    matcher             = "200"
    healthy_threshold   = 2
    unhealthy_threshold = 3
    timeout             = 5
    interval            = 30
  }

  tags = {
    Name = "shopping-gateway-target-group"
  }
}



#spring cloud gateway task definition
resource "aws_ecs_task_definition" "gateway" {
  family = "shopping-gateway"
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
      name = "gateway"
      # Docker image that ECS should pull from ECR
      image = "${aws_ecr_repository.gateway.repository_url}:latest"
      # If this container stops, the task is considered stopped
      essential = true
      # Port exposed by the Eureka container
      portMappings = [
        {
          containerPort = 9090
          protocol      = "tcp"
        }
      ]

      environment = [
        {
          name  = "FRONTEND_URL"
          value = "http://localhost:5173"
        },
        {
          name  = "EUREKA_URL"
          value = "http://eureka.shopping.local:8761/eureka/"
        }
      ]

      secrets = [
        {
          name      = "SECRET_KEY"
          valueFrom = aws_secretsmanager_secret.jwt_secret.arn
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.gateway.name
          "awslogs-region"        = "ap-south-1"
          "awslogs-stream-prefix" = "gateway"
        }
      }
    }

  ])

  # AWS tag
  tags = {
    Name = "shopping-gateway-task"
  }
}

#create service for gateway to run tasks
resource "aws_ecs_service" "gateway" {
  # Name of the ECS service
  name = "shopping-gateway-service"
  # ECS cluster where the task will run
  cluster = aws_ecs_cluster.main.id
  # Task Definition that describes how to run gateway
  task_definition = aws_ecs_task_definition.gateway.arn
  # Keep 1 Eureka task running
  desired_count = 1
  # Use AWS Fargate to run the task
  launch_type = "FARGATE"
  # Network configuration for the ECS task
  network_configuration {
    subnets         = [aws_subnet.private.id]
    security_groups = [aws_security_group.ecs.id]
    # Do NOT give the task a public IP
    assign_public_ip = false
  }
  load_balancer {
    container_name   = "gateway"
    container_port   = 9090
    target_group_arn = aws_lb_target_group.gateway.arn
  }

  tags = {
    Name = "shopping-gateway-service"
  }
  depends_on = [
    aws_lb_listener.gateway
  ]
}

#create ecs task definition for authentication service
resource "aws_ecs_task_definition" "authentication" {
  family                   = "shopping-authentication"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.ecs_task_execution.arn
  container_definitions = jsonencode([
    {
      name      = "authentication"
      image     = "${aws_ecr_repository.authentication.repository_url}:latest"
      essential = true
      portMappings = [
        {
          containerPort = 8084
          protocol      = "tcp"
        }
      ]

      environment = [
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = "prod"
        },
        {
          name  = "DB_TYPE"
          value = "mysql"
        },
        {
          name  = "DB_HOSTNAME"
          value = aws_db_instance.mysql.address
        },
        {
          name  = "DB_PORT",
          value = "3306"
        },
        {
          name  = "DB_NAME"
          value = "authentication_db"
        },
        {
          name  = "DB_USERNAME"
          value = aws_db_instance.mysql.username
        },
        {
          name  = "DB_DRIVER_CLASS_NAME"
          value = "com.mysql.cj.jdbc.Driver"
        },
        {
          name  = "EUREKA_URL"
          value = "http://eureka.shopping.local:8761/eureka/"
        }
      ]
      secrets = [
        {
          name      = "DB_PASSWORD"
          valueFrom = "${aws_db_instance.mysql.master_user_secret[0].secret_arn}:password::"
        },
        {
          name      = "SECRET_KEY"
          valueFrom = aws_secretsmanager_secret.jwt_secret.arn
        }
      ]

      logConfiguration = {
        logDriver = "awslogs"

        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.authentication.name
          "awslogs-region"        = "ap-south-1"
          "awslogs-stream-prefix" = "authentication"
        }
      }
    }
  ])
  tags = {
    Name = "shopping-authentication-task"
  }
}

#create actual ecs to run the ecs task definition instance
resource "aws_ecs_service" "authentication" {
  name            = "shopping-authentication-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.authentication.arn
  desired_count   = 1
  launch_type     = "FARGATE"
  network_configuration {
    subnets = [
      aws_subnet.private.id
    ]
    security_groups = [
      aws_security_group.ecs.id
    ]
    assign_public_ip = false
  }
  depends_on = [
    aws_ecs_cluster.main
  ]
  tags = {
    Name = "shopping-authentication-service"
  }
}

#create log resouce to capture authentication service logs
resource "aws_cloudwatch_log_group" "authentication" {
  name              = "/ecs/shopping-authentication"
  retention_in_days = 1

  tags = {
    Name = "shopping-authentication-logs"
  }
}