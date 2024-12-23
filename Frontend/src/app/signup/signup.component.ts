import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { SnackbarService } from '../services/snackbar.service';
import { MatDialogRef } from '@angular/material/dialog';
import { GlobalConstants } from '../shared/global-constants';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.scss']
})
export class SignupComponent implements OnInit {
  password = true;
  confirmPasswordVisible = false;
  signupForm!: FormGroup;
  responseMessage: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService,
    private snackBarService: SnackbarService,
    public dialogRef: MatDialogRef<SignupComponent>
  ) {}

  ngOnInit(): void {
    this.signupForm = this.formBuilder.group({
      name: [null, [Validators.required, Validators.pattern(GlobalConstants.nameRegex)]],
      email: [null, [Validators.required, Validators.pattern(GlobalConstants.emailRegex)]],
      phone: [null, [Validators.required, Validators.pattern(GlobalConstants.contactNumberRegex)]],
      password: [null, [Validators.required]],
      confirmPassword: [null, [Validators.required]],
    });
  }

  validateSubmit(): boolean {
    return (
      this.signupForm.controls['password'].value !== this.signupForm.controls['confirmPassword'].value
    );
  }

  handleSubmit(): void {
    if (this.signupForm.invalid || this.validateSubmit()) return;

    const formData = this.signupForm.value;
    const data = {
      name: formData.name,
      email: formData.email,
      phone: formData.phone,
      password: formData.password
    };

    this.userService.signup(data).subscribe(
      (response: any) => {
        //this.dialogRef.close();
        this.responseMessage = response?.message;
        this.snackBarService.openSnackBar(this.responseMessage, '');
        this.router.navigate(['/']);
      },
      (error: any) => {
        this.responseMessage = error.error?.message || GlobalConstants.genericError;
        this.snackBarService.openSnackBar(this.responseMessage, GlobalConstants.error);
      }
    );
  }
}
