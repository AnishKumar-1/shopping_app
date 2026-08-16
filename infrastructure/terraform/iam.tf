#IAM ROLE creation three levels
#NAME        → What is it called? role name
#TRUST       → Who can use it? example below for ecs
#PERMISSION  → What can they do? policy

# Creates an AWS IAM Role
# aws_iam_role = Terraform resource type
# ecs_task_execution = Terraform reference name
resource "aws_iam_role" "ecs_task_execution" {

  # Actual name of the IAM Role that will appear in AWS Console
  name = "shopping-app-ecs-task-execution-role"

  # Trust Policy:
  # Defines WHO is allowed to assume/use this IAM Role
  assume_role_policy = jsonencode({

    # IAM policy language version
    Version = "2012-10-17"

    # Contains the rules for this trust policy
    Statement = [
      {
        # Allow the specified principal to perform the action
        Effect = "Allow"

        # WHO is trusted to use/assume this role?
        # Here: ECS Tasks
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }

        # What can the trusted principal do?
        # ECS Tasks can assume this IAM Role
        #sts means AWS Security Token Service (STS)
        #AssumeRole means: "Take on the permissions associated with this role temporarily."
        # Together "ECS Tasks are trusted, and they are allowed to assume this IAM role."
        Action = "sts:AssumeRole"
      }
    ]
  })

  # AWS tags for identifying the IAM Role
  # Tags do NOT provide permissions
  tags = {
    Name = "shopping-app-ecs-task-execution-role"
  }
}


# Attach the AWS-managed ECS Task Execution policy to our IAM Role.
# This gives ECS tasks the standard permissions needed to:
# - Pull Docker images from Amazon ECR
# - Send container logs to CloudWatch Logs
resource "aws_iam_role_policy_attachment" "ecs_task_execution" {
  # The AWS-managed policy we want to attach
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
  # The IAM Role that receives the permissions
  role = aws_iam_role.ecs_task_execution.name
}