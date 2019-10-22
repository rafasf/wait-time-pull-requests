# Wait Time For Pull Requests

[![Actions Status](https://github.com/rafasf/wait-time-pull-requests/workflows/CI/badge.svg)](https://github.com/rafasf/wait-time-pull-requests/actions)

## What?

Have you ever wondered how long have you waited for a piece of code to be
integrated to the mainline (a.k.a. master, trunk) when the team uses pull
requests?

Pull Requests have become common in many organisations, and just like any other
process it can either help or make things less efficient.

If you don't use tooling like [Pull
Panda](https://github.com/marketplace/pull-panda) it might be hard to identify
if the wait time generated by this phase of the process is healthy or not. With
this very developer-centric and manual tool, you can get those numbers. :smile:

## Usage

> It only works with public repositories for now.

```sh
$ lein run -- -o[REPO OWNER] -r [REPO NAME]
```

## TODO

- [ ] Support authentication
- [ ] Calculate average and mean time
- [ ] Save source as CSV -- enable some sheet graphing fun
- [ ] Support arguments -- lower the entrance barrier by creating an executable


