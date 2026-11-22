
# =====================================================
# 1. Amazon ECR Repository
# =====================================================

resource "aws_ecr_repository" "product_service" {
  name                 = "product-service"
  image_tag_mutability = "MUTABLE"

  # Scan images when they are pushed
  image_scanning_configuration {
    scan_on_push = true
  }

  # Encrypt images at rest
  encryption_configuration {
    encryption_type = "AES256"
  }

  tags = {
    Name        = "product-service"
    Environment = "learning"
    ManagedBy   = "Terraform"
  }
}


# =====================================================
# 2. GitHub OIDC Identity Provider
# =====================================================

resource "aws_iam_openid_connect_provider" "github" {
  url = "https://token.actions.githubusercontent.com"

  client_id_list = [
    "sts.amazonaws.com"
  ]

  tags = {
    Name      = "github-actions-oidc"
    ManagedBy = "Terraform"
  }
}


# =====================================================
# 3. GitHub IAM Trust Policy
# Defines WHO can assume the IAM role
# =====================================================

data "aws_iam_policy_document" "github_assume_role" {

  statement {
    effect = "Allow"

    actions = [
      "sts:AssumeRoleWithWebIdentity"
    ]

    principals {
      type = "Federated"

      identifiers = [
        aws_iam_openid_connect_provider.github.arn
      ]
    }

    # Validate GitHub's audience
    condition {
      test     = "StringEquals"
      variable = "token.actions.githubusercontent.com:aud"

      values = [
        "sts.amazonaws.com"
      ]
    }

    # Only this repository's develop branch is trusted
    condition {
      test     = "StringEquals"
      variable = "token.actions.githubusercontent.com:sub"

      values = [
        "repo:AnishKumar-1/shopping_app:ref:refs/heads/main"
      ]
    }
  }
}


# =====================================================
# 4. IAM Role for GitHub Actions
# =====================================================

resource "aws_iam_role" "github_actions_ecr" {
  name = "GitHubActionsECRRole"

  assume_role_policy = data.aws_iam_policy_document.github_assume_role.json

  tags = {
    Name      = "GitHubActionsECRRole"
    ManagedBy = "Terraform"
  }
}


# =====================================================
# 5. ECR Push Permissions Policy
# Defines WHAT the IAM role can do
# =====================================================

data "aws_iam_policy_document" "ecr_push_policy" {

  # ECR authentication
  statement {
    sid    = "ECRAuthorization"
    effect = "Allow"

    actions = [
      "ecr:GetAuthorizationToken"
    ]

    resources = ["*"]
  }

  # Upload Docker images to Product Service repository
  statement {
    sid    = "ECRPushImages"
    effect = "Allow"

    actions = [
      "ecr:BatchCheckLayerAvailability",
      "ecr:InitiateLayerUpload",
      "ecr:UploadLayerPart",
      "ecr:CompleteLayerUpload",
      "ecr:PutImage"
    ]

    resources = [
      aws_ecr_repository.product_service.arn
    ]
  }
}


# =====================================================
# 6. Attach ECR Permissions to IAM Role
# =====================================================

resource "aws_iam_role_policy" "github_ecr_push" {
  name = "GitHubECRPushPolicy"

  role = aws_iam_role.github_actions_ecr.id

  policy = data.aws_iam_policy_document.ecr_push_policy.json
}


# =====================================================
# 7. Terraform Outputs
# =====================================================

output "product_ecr_repository_url" {
  description = "Amazon ECR repository URL for Product Service"
  value       = aws_ecr_repository.product_service.repository_url
}

output "github_actions_role_arn" {
  description = "IAM role ARN assumed by GitHub Actions"
  value       = aws_iam_role.github_actions_ecr.arn
}
