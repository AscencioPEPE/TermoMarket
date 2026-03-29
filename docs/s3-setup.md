# S3 Setup

## Local Configuration

This project uses the AWS SDK default credentials provider chain.

For local development, set these environment variables before running the backend:

### PowerShell

```powershell
$env:AWS_ACCESS_KEY_ID='YOUR_ACCESS_KEY_ID'
$env:AWS_SECRET_ACCESS_KEY='YOUR_SECRET_ACCESS_KEY'
$env:S3_BUCKET_NAME='thermomarket-assets-dev'
$env:S3_REGION='us-east-1'
```

### cmd

```bat
set AWS_ACCESS_KEY_ID=YOUR_ACCESS_KEY_ID
set AWS_SECRET_ACCESS_KEY=YOUR_SECRET_ACCESS_KEY
set S3_BUCKET_NAME=thermomarket-assets-dev
set S3_REGION=us-east-1
```

Then run the app normally.

## Production Configuration

For production on EC2, do not use access keys in environment variables.
Use an IAM Role attached to the instance.

Set only:

- `S3_BUCKET_NAME`
- `S3_REGION`
- optionally `S3_PUBLIC_BASE_URL`

The AWS SDK will automatically resolve credentials from the instance role.

## Security Note

If an access key or secret key is ever pasted into chat, screenshots, commits, or shared notes,
rotate it immediately in IAM and update your local environment variables.
