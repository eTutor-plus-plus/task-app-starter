<!-- omit in toc -->

# Contributing to eTutor Task App Starter

First off, thanks for taking the time to contribute! ❤️

All types of contributions are encouraged and valued. See the [Table of Contents](#table-of-contents) for different ways to help and details about how this project handles them.
Please make sure to read the relevant section before making your contribution. It will make it a lot easier for us maintainers and smooth out the experience for all involved.

<!-- omit in toc -->

## Table of Contents

- [I Have a Question](#i-have-a-question)
- [I Want To Contribute](#i-want-to-contribute)
    - [Reporting Bugs](#reporting-bugs)
    - [Suggesting Enhancements](#suggesting-enhancements)
    - [Your First Code Contribution](#your-first-code-contribution)
- [Styleguides](#styleguides)
    - [Versioning](#versioning)
    - [Commit Messages](#commit-messages)
    - [Branches](#branches)
    - [Release Process](#release-process)

## I Have a Question

> If you want to ask a question, we assume that you have read the available [Documentation](https://martin-temp.github.io/task-app-starter/).

Before you ask a question, it is best to search for existing [Issues](https://github.com/martin-temp/task-app-starter/issues) that might help you. In case you have found a suitable
issue and still need clarification, you can write your question in this issue. It is also advisable to search the internet for answers first.

If you then still feel the need to ask a question and need clarification, we recommend the following:

- Open an [Issue](https://github.com/martin-temp/task-app-starter/issues/new/choose).
- Provide as much context as you can about what you're running into.
- Provide project and platform versions (Java, nodejs, npm, etc), depending on what seems relevant.

## I Want To Contribute

> ### Legal Notice <!-- omit in toc -->
> When contributing to this project, you must agree that you have authored 100% of the content, that you have the necessary rights to the content and that the content you
> contribute may be provided under the project license.

### Reporting Bugs

<!-- omit in toc -->

#### Before Submitting a Bug Report

A good bug report shouldn't leave others needing to chase you up for more information. Therefore, we ask you to investigate carefully, collect information and describe the issue in
detail in your report. Please complete the following steps in advance to help us fix any potential bug as fast as possible.

- Make sure that you are using the latest version.
- Determine if your bug is really a bug and not an error on your side e.g. using incompatible environment components/versions (Make sure that you have read
  the [documentation](https://martin-temp.github.io/task-app-starter/). If you are looking for support, you might want to check [this section](#i-have-a-question)).
- To see if other users have experienced (and potentially already solved) the same issue you are having, check if there is not already a bug report existing for your bug or error
  in the [bug tracker](https://github.com/martin-temp/task-app-starter/issues).
- Collect information about the bug:
    - Stack trace (Traceback)
    - OS, Platform and Version (Windows, Linux, macOS, x86, ARM)
    - Version of the interpreter, compiler, SDK, runtime environment, package manager, depending on what seems relevant.
    - Possibly your input and the output
    - Can you reliably reproduce the issue? And can you also reproduce it with older versions?

<!-- omit in toc -->

#### How Do I Submit a Good Bug Report?

> You must never report security related issues, vulnerabilities or bugs including sensitive information to the issue tracker, or elsewhere in public. Instead, sensitive bugs must
> be sent by email.

We use GitHub issues to track bugs and errors. If you run into an issue with the project:

- Open an [Issue](https://github.com/martin-temp/task-app-starter/issues/new/choose).
- Explain the behavior you would expect and the actual behavior.
- Please provide as much context as possible and describe the *reproduction steps* that someone else can follow to recreate the issue on their own. This usually includes your code.
  For good bug reports you should isolate the problem and create a reduced test case.
- Provide the information you collected in the previous section.

Once it's filed:

- The project team will label the issue accordingly.
- A team member will try to reproduce the issue with your provided steps. If there are no reproduction steps or no obvious way to reproduce the issue, the team will ask you for
  those steps and mark the issue as `Status: Awaiting Repro`. Bugs with the `Status: Awaiting Repro` tag will not be addressed until they are reproduced.
- If the team is able to reproduce the issue, it will be marked `Status: WIP` when work is started on the issue, as well as possibly other tags (such as `Priority: High +`), and
  the issue will be left to be [implemented by someone](#your-first-code-contribution).

### Suggesting Enhancements

This section guides you through submitting an enhancement suggestion for eTutor Task App Starter, **including completely new features and minor improvements to existing
functionality**. Following these guidelines will help maintainers and the community to understand your suggestion and find related suggestions.

<!-- omit in toc -->

#### Before Submitting an Enhancement

- Make sure that you are using the latest version.
- Read the [documentation](https://martin-temp.github.io/task-app-starter/) carefully and find out if the functionality is already covered, maybe by an individual configuration.
- Perform a [search](https://github.com/martin-temp/task-app-starter/issues) to see if the enhancement has already been suggested. If it has, add a comment to the existing issue
  instead of opening a new one.
- Find out whether your idea fits with the scope and aims of the project. It's up to you to make a strong case to convince the project's developers of the merits of this feature.
  Keep in mind that we want features that will be useful to the majority of our users and not just a small subset.

<!-- omit in toc -->

#### How Do I Submit a Good Enhancement Suggestion?

Enhancement suggestions are tracked as [GitHub issues](https://github.com/martin-temp/task-app-starter/issues).

- Use a **clear and descriptive title** for the issue to identify the suggestion.
- Provide a **step-by-step description of the suggested enhancement** in as many details as possible.
- **Describe the current behavior** and **explain which behavior you expected to see instead** and why. At this point you can also tell which alternatives do not work for you.
- **Explain why this enhancement would be useful** to most eTutor Task App Starter users. You may also want to point out the other projects that solved it better and which could
  serve as inspiration.

### Your First Code Contribution

Following dependencies are required to build and run this project:

* Java JDK >= 21
* Docker and Docker Compose
* Maven
* Node.js >= 20 (only for creating new releases)
* You can use any Java IDE of your choice.

## Styleguides

### Versioning

This project uses [Semantic Versioning](https://semver.org/).

### Commit Messages

Commit messages have to follow the [Conventional Commits Specification](https://www.conventionalcommits.org/en/v1.0.0/).

You can use the [Conventional Commit](https://plugins.jetbrains.com/plugin/13389-conventional-commit) plugin in IntelliJ to build your commit messages or the command line
tool [Commitizen](http://commitizen.github.io/cz-cli/).

Issues can be automatically closed by adding the issue number to the commit message body (e.g. `Closes: #123`). The Issue will be closed when the PR is merged into the `main`
branch.

If a commit does not fix an issue, but is related to it, you can add the issue number to the commit message footer (e.g. `Refs: #123`).

### Branches

This project follows the rules of [gitflow](https://danielkummer.github.io/git-flow-cheatsheet/) for branch naming.

The `main` branch contains the latest release. The `develop` branch contains the latest development changes.
New features are developed in feature branches (prefix `feature/`) and merged into the develop branch when finished.
New releases are created from the develop branch.

### Release Process

New releases can only be published by repository owners.

In order to release a new version you have to conduct following steps:

1. Run `npm install` if you have not done that so far.
2. In the `develop` branch, run `npm run release -- --no-verify [--sign] [--prerelease beta] [--dry-run]`
    * Use `--sign` if you have configured a GPG key
    * Use `--prerelease XXX` to publish a prerelease (beta or alpha).
    * Use `--dry-run` to perform a dry run without any changes.
3. Delete the created tag (e.g. `git tag -d v2.0.0`). This is necessary because the tag is created by the release script, but we want the tag in the main branch, not the develop
   branch.
4. Push the changes to the `develop` branch.
    * The GitHub Action `Release Library` automatically creates a new pull-request.
5. Merge the PR into the main branch.
    * The GitHub Action `Publish Library` automatically creates a new release.
    * The project is built and published automatically using GitHub Actions.
6. Publish the drafted release.

<!-- omit in toc -->

## Attribution

This guide is based on the **contributing-gen**. [Make your own](https://github.com/bttger/contributing-gen)!
